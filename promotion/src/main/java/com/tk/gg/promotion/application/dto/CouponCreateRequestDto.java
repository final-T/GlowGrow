package com.tk.gg.promotion.application.dto;

import com.tk.gg.promotion.domain.enums.DiscountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CouponCreateRequestDto {
    private UUID promotionId;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Integer totalQuantity;
}
