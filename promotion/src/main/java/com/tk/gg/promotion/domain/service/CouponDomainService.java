package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueResponseDto;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.CouponUser;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
