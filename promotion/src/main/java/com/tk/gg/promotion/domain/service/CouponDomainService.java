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
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponDomainService {
    private final PromotionRepository promotionRepository;
    private final CouponUserRepository couponUserRepository;

    @Transactional
    public Coupon createCoupon(CouponCreateRequestDto requestDto) {
        Promotion promotion = promotionRepository.findById(requestDto.getPromotionId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));

        // 쿠폰 생성 로직을 Promotion 애그리거트에서 처리
        return promotion.createCoupon(
                requestDto.getDescription(),
                requestDto.getDiscountType(),
                requestDto.getDiscountValue(),
                requestDto.getMaxDiscount(),
                requestDto.getValidFrom(),
                requestDto.getValidUntil(),
                requestDto.getTotalQuantity());
    }

    // TODO : 대규모 트래픽 발생 시, 쿠폰 발급 로직을 변경해야 함.
    @Transactional
    public CouponIssueResponseDto issueCoupoon(CouponIssueRequestDto requestDto) {
        // 프로모션 조회
        Promotion promotion = promotionRepository.findById(requestDto.getPromotionId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));

        // 발급하려는 쿠폰 조회
        Coupon coupon = promotion.getCoupons().stream()
                .filter(c -> c.getCouponId().equals(requestDto.getCouponId()))
                .findFirst()
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COUPON_NO_EXIST));

        // TODO: 중복 발급 방지 로직 추가
        boolean isCouponAlreadyIssued = couponUserRepository.existsByCouponAndUserId(coupon.getCouponId(), requestDto.getUserId());
        if (isCouponAlreadyIssued) {
            // 중복 발급 예외 처리
            throw new GlowGlowException(GlowGlowError.COUPON_ALREADY_ISSUED);
        }


        // 쿠폰 발급
        coupon.issueCoupon();

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

    // 사용자 쿠폰 목록 조회
    @Transactional(readOnly = true)
    public List<CouponUser> getUserCoupons(Long userId) {
        return couponUserRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public CouponUser getUserCoupon(Long userId, UUID couponId) {

        return couponUserRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COUPON_NO_EXIST));
    }

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
