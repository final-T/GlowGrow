package com.tk.gg.payment.domain.type;

public enum PaymentStatus {
    REQUESTED("요청됨"),
    COMPLETED("완료"),
    FAILED("실패");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
