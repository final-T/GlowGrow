package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponDomainService {
    private final PromotionRepository promotionRepository;

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
}
