package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueResponseDto;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.CouponUser;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.CouponStatus;
import com.tk.gg.promotion.infrastructure.repository.CouponRepository;
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import com.tk.gg.promotion.infrastructure.repository.RedisRepository;
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

    private static final String COUPON_STOCK_KEY_PREFIX = "coupon:stock:";
    private static final String COUPON_ISSUED_SET_KEY_PREFIX = "coupon:issued:set:";

    /**
     * 쿠폰 생성 시 RDBMS에 쿠폰 정보를 저장하고, Redis에 총 재고 수량과 발급 수량을 0으로 초기화합니다.
     * @param requestDto 쿠폰 생성 요청 DTO
     * @return 생성된 쿠폰
     */
    @Transactional
    public Coupon createCoupon(CouponCreateRequestDto requestDto) {
        Promotion promotion = promotionRepository.findById(requestDto.getPromotionId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PROMOTION_NO_EXIST));

        // 쿠폰 생성 로직을 Promotion 에서 분리
        Coupon coupon = Coupon.builder()
                .promotion(promotion)
                // TODO : 일단 RANDOM CODE로 생성
                .code(UUID.randomUUID().toString()) // CouponCodeGenerator
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

    @Transactional
    public CouponIssueResponseDto issueCoupoon(CouponIssueRequestDto requestDto) {
        String stockKey = COUPON_STOCK_KEY_PREFIX + requestDto.getCouponId().toString();
        String issuedSetKey = COUPON_ISSUED_SET_KEY_PREFIX + requestDto.getCouponId().toString();

        // 사용자 중복 발급 확인
        Boolean isAlreadyIssued = redisRepository.sIsMember(issuedSetKey, requestDto.getUserId().toString());
        if (Boolean.TRUE.equals(isAlreadyIssued)) {
            throw new GlowGlowException(GlowGlowError.COUPON_ALREADY_ISSUED);
        }

        // 쿠폰 재고 감소 처리 (Redis)
        Long remainingStock = redisRepository.decrement(stockKey);
        if (remainingStock < 0) {
            // 쿠폰 재고 부족 예외 처리
            throw new GlowGlowException(GlowGlowError.COUPON_QUANTITY_EXCEEDED);
        }

        // 사용자 ID를 발급된 쿠폰 Set에 추가
        redisRepository.sAdd(issuedSetKey, String.valueOf(requestDto.getUserId()));

        // TODO: 카프카를 이용하여 비동기적으로 쿠폰 사용자에게 쿠폰 발급 이벤트 전달
        // 발급하려는 쿠폰 조회
        Coupon coupon = couponRepository.findById(requestDto.getCouponId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COUPON_NO_EXIST));

        // 쿠폰 사용자 생성
        CouponUser couponUser = CouponUser.builder()
                .coupon(coupon)
                .userId(requestDto.getUserId())
                .build();

        couponUserRepository.save(couponUser);

        return CouponIssueResponseDto.builder()
                .couponId(coupon.getCouponId())
                .couponDescription(coupon.getDescription())
                .userId(couponUser.getUserId())
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
