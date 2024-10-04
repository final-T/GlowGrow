package com.tk.gg.promotion.domain.enums;

public enum CouponStatus {
    ACTIVE("활성화"),
    EXPIRED("만료"),
    USED("사용됨");


    final String description;

    CouponStatus(String description) {
        this.description = description;
    }
}
