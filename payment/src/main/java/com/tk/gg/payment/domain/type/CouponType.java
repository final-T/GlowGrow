package com.tk.gg.payment.domain.type;

public enum CouponType {
    GLOWGROW("운영 측"),
    PROVIDER("서비스 제공자 측"),
    NONE("쿠폰 없음");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
