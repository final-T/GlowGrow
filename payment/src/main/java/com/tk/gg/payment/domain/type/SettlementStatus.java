package com.tk.gg.payment.domain.type;

import lombok.Getter;

@Getter
public enum SettlementStatus {
    PENDING("대기 중"),
    PROCESSING("처리 중"),
    COMPLETED("완료"),
    FAILED("실패"),
    CANCELED("취소됨");

    private final String description;

    SettlementStatus(String description) {
        this.description = description;
    }

}
