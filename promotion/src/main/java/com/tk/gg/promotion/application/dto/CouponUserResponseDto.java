package com.tk.gg.promotion.application.dto;

import com.tk.gg.promotion.domain.CouponUser;
import com.tk.gg.promotion.domain.enums.CouponStatus;
import com.tk.gg.promotion.domain.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CouponUserResponseDto {
    private UUID couponId;
    private UUID promotionId;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private CouponStatus status;
    private Boolean isUsed;

    public static CouponUserResponseDto from(CouponUser couponUser) {
        return CouponUserResponseDto.builder()
                .couponId(couponUser.getCoupon().getCouponId())
                .promotionId(couponUser.getCoupon().getPromotion().getPromotionId())
                .description(couponUser.getCoupon().getDescription())
                .discountType(couponUser.getCoupon().getDiscountType())
                .discountValue(couponUser.getCoupon().getDiscountValue())
                .maxDiscount(couponUser.getCoupon().getMaxDiscount())
                .validFrom(couponUser.getCoupon().getValidFrom())
                .validUntil(couponUser.getCoupon().getValidUntil())
                .status(couponUser.getCoupon().getStatus())
                .isUsed(couponUser.isUsed())
                .build();
    }
}
