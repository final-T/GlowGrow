package com.tk.gg.common.kafka.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@JsonSerialize
@JsonDeserialize
public class CouponUserResponseDto {
    private UUID couponId;
    private String couponcode;
    private UUID promotionId;
    private String description;
    private String discountType;    // PERCENTAGE, AMOUNT
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String status;      //     ACTIVE, EXPIRED
    private Boolean isUsed;

}
