package com.tk.gg.promotion.application.dto;

import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.enums.CouponStatus;
import com.tk.gg.promotion.domain.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CouponResponseDto {
    private UUID couponId;
    private UUID promotionId;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Integer totalQuantity;
    private CouponStatus status;

    public static CouponResponseDto from(Coupon coupon) {
        return CouponResponseDto.builder()
                .couponId(coupon.getCouponId())
                .promotionId(coupon.getPromotion().getPromotionId())
                .description(coupon.getDescription())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .maxDiscount(coupon.getMaxDiscount())
                .validFrom(coupon.getValidFrom())
                .validUntil(coupon.getValidUntil())
                .totalQuantity(coupon.getTotalQuantity())
                .status(coupon.getStatus())
                .build();
    }
}
