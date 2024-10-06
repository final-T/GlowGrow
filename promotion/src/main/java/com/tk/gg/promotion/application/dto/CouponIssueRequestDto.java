package com.tk.gg.promotion.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CouponIssueRequestDto {
    private UUID promotionId;
    private UUID couponId;
    private Long userId;
}
