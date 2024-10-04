package com.tk.gg.promotion.domain.enums;

public enum DiscountType {
    PERCENTAGE("퍼센트"),
    AMOUNT("금액");

    final String description;

    DiscountType(String description) {
        this.description = description;
    }
}
