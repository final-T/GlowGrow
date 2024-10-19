package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueResponseDto;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.CouponUser;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.CouponStatus;
import com.tk.gg.promotion.infrastructure.messaging.CouponIssueEvent;
import com.tk.gg.promotion.infrastructure.messaging.CouponKafkaProducer;
import com.tk.gg.promotion.infrastructure.repository.CouponRepository;
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import com.tk.gg.promotion.infrastructure.repository.RedisRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "COUPON-DOMAIN-SERVICE")
@RequiredArgsConstructor
public class CouponDomainService {
    private final PromotionRepository promotionRepository;
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final RedisRepository redisRepository;
    private final CouponKafkaProducer couponKafkaProducer;

    private static final String MASTER_COUPON_CODE_PREFIX = "GlowGrow";

    private static final String COUPON_STOCK_KEY_PREFIX = "coupon:stock:";
    private static final String COUPON_ISSUED_SET_KEY_PREFIX = "coupon:issued:set:";

    /**
     * 쿠폰 생성 시 RDBMS에 쿠폰 정보를 저장하고, Redis에 총 재고 수량과 발급 수량을 0으로 초기화합니다.
     * @param requestDto 쿠폰 생성 요청 DTO
     * @return 생성된 쿠폰
     */
    @Transactional
    public Coupon createCoupon(CouponCreateRequestDto requestDto, AuthUserInfo userInfo) {
        Promotion promotion = promotionRepository.findById(requestDto.getPromotionId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PROMOTION_NO_EXIST));

        // 쿠폰 생성 주체의 권한이 MASTER(SYSTEM)이면 GlowGrow + UUID로 생성
        // 쿠폰 생성 주체의 권한이 PROVIDER(서비스 제공자)이면 userID + UUID로 생성
        String couponCode = generateCouponCode(userInfo);

        // 쿠폰 생성 로직을 Promotion 에서 분리
        Coupon coupon = Coupon.builder()
                .promotion(promotion)
                .code(couponCode)
                .description(requestDto.getDescription())
                .discountType(requestDto.getDiscountType())
                .discountValue(requestDto.getDiscountValue())
                .maxDiscount(requestDto.getMaxDiscount())
                .validFrom(requestDto.getValidFrom())
                .validUntil(requestDto.getValidUntil())
                .totalQuantity(requestDto.getTotalQuantity())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        // Redis에 쿠폰 총 재고 수량 저장
        String stockKey = COUPON_STOCK_KEY_PREFIX + savedCoupon.getCouponId().toString();
        redisRepository.set(stockKey, savedCoupon.getTotalQuantity().toString());

        return coupon;
    }

    /**
     * 쿠폰 코드를 생성합니다. MASTER 권한은 GlowGrow + UUID, PROVIDER 권한은 userID + UUID로 생성합니다.
     * @param userInfo 유저 정보
     * @return 생성된 쿠폰 코드
     */
    private String generateCouponCode(AuthUserInfo userInfo) {
        if (userInfo.getUserRole().equals(UserRole.MASTER)) {
            return MASTER_COUPON_CODE_PREFIX + "-" + UUID.randomUUID();
        } else if (userInfo.getUserRole().equals(UserRole.PROVIDER)) {
            return userInfo.getId().toString() + "-" + UUID.randomUUID();
        }
        throw new GlowGlowException(GlowGlowError.AUTH_INVALID_CREDENTIALS);
    }

    public CouponIssueResponseDto issueCoupoon(CouponIssueRequestDto requestDto) {
        String stockKey = COUPON_STOCK_KEY_PREFIX + requestDto.getCouponId().toString();
        String issuedSetKey = COUPON_ISSUED_SET_KEY_PREFIX + requestDto.getCouponId().toString();

        // Lua Script: 중복 발급 확인, 쿠폰 재고 확인, 쿠폰 재고 감소, 사용자 ID를 발급된 쿠폰 Set에 추가
        String script =
                "local stockKey = KEYS[1];" +
                "local issuedSetKey = KEYS[2];" +
                "local userId = ARGV[1];" +
                "local stock = tonumber(redis.call('get', stockKey));" +
                "if stock == nil or stock <= 0 then return -1; end;" + // 재고 부족 시 -1 반환
                "local isMember = redis.call('SISMEMBER', issuedSetKey, userId);" +
                "if isMember == 1 then return -2; end;" + // 중복 발급 시 -2 반환
                "redis.call('decr', stockKey);" + // 재고 감소
                "redis.call('sadd', issuedSetKey, userId);" + // 발급된 쿠폰 Set에 사용자 ID 추가
                "return 1;"; // 성공 시 1 반환

        // Redis Lua Script 실행
        Long result = redisRepository.executeLuaScript(script,
                List.of(stockKey, issuedSetKey),  // keys로 Redis 키를 전달
                List.of(requestDto.getUserId().toString())  // args로 인자를 전달
        );

        if (result == -1) {
            throw new GlowGlowException(GlowGlowError.COUPON_QUANTITY_EXCEEDED);
        } else if (result == -2) {
            throw new GlowGlowException(GlowGlowError.COUPON_ALREADY_ISSUED);
        }

        // 카프카를 이용하여 비동기적으로 쿠폰 사용자에게 쿠폰 발급 이벤트 전달

        log.info("사용자 쿠폰 발급 이벤트 전송 : {}", requestDto);
        CouponIssueEvent couponIssueEvent = new CouponIssueEvent(requestDto.getCouponId(), requestDto.getUserId());
        couponKafkaProducer.sendCouponIssueEvent(couponIssueEvent);

        return CouponIssueResponseDto.builder()
                .couponId(requestDto.getCouponId())
                .userId(requestDto.getUserId())
                .build();
    }

    /**
     * 사용자가 발급받은 쿠폰 목록을 조회합니다.
     * @param userId 사용자 ID
     * @return 사용자가 발급받은 쿠폰 목록
     */
    @Transactional(readOnly = true)
    public List<CouponUser> getUserCoupons(Long userId) {
        return couponUserRepository.findByUserId(userId);
    }

    /**
     * 사용자가 발급받은 쿠폰을 단건 조회합니다.
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     * @return 사용자가 발급받은 쿠폰
     */
    @Transactional(readOnly = true)
    public CouponUser getUserCoupon(Long userId, UUID couponId) {

        return couponUserRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COUPON_NO_EXIST));
    }

    /**
     * 사용자가 발급받은 쿠폰을 사용합니다.
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     */
    @Transactional
    public void useCoupon(Long userId, UUID couponId) {
        // 사용자와 연관된 쿠폰을 조회
        CouponUser couponUser = couponUserRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COUPON_NO_EXIST));

        // 1차 검증은 결제에서 쿠폰 사용 가능 여부 확인, 2차 검증으로 수행
        if (couponUser.isUsed()) {
            throw new GlowGlowException(GlowGlowError.COUPON_ALREADY_USED);
        }

        // 1차 검증은 결제에서 쿠폰 만료 여부 확인, 2차 검증 수행
        if (couponUser.getCoupon().getStatus().equals(CouponStatus.EXPIRED)) {
            throw new GlowGlowException(GlowGlowError.COUPON_EXPIRED);
        }

        // 쿠폰 사용 처리
        couponUser.useCoupon();
    }
}
