package com.tk.gg.promotion.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class CouponIssueResponseDto {
    private UUID couponId;
    private Long userId;

    @Builder
    public CouponIssueResponseDto(UUID couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }
}
