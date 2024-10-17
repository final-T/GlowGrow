package com.tk.gg.reservation.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationStatus {
    CHECK("접수"),
    ACCEPT("승인"),
    REFUSED("거절"),
    DONE("서비스 후 완료"),
    CANCEL("취소"),
    PAYMENT_CALL("결제 요청");

    private final String description;
}
