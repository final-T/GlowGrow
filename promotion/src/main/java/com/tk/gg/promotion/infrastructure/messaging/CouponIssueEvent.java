package com.tk.gg.promotion.infrastructure.messaging;

import lombok.Data;

import java.util.UUID;

@Data
public class CouponIssueEvent {
    private UUID couponId;
    private Long userId;

    // 기본 생성자 추가
    public CouponIssueEvent() {
    }

    public CouponIssueEvent(UUID couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }
}
