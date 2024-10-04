package com.tk.gg.promotion.domain.enums;

public enum PromotionStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    final String status;

    PromotionStatus(String status) {
        this.status = status;
    }
}
