package com.tk.gg.payment.domain.type;

public enum RefundType {
    REFUND_REQUESTED("환불 요청"),
    REFUND_COMPLETED("환불 완료"),
    REFUND_FAILED("환불 실패");

    private String description;

    RefundType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
