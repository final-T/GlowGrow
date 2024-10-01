package com.tk.gg.promotion.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CouponIssueRequestDto {
    private UUID promotionId;
    private UUID couponId;
    private Long userId;
}
