package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.domain.type.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateReservationStatusRequest(
        @NotNull(message = "예약 상태는 필수입니다.")
        String reservationStatus
) {
    public ReservationStatus getReservationStatus() {
        return ReservationStatus.valueOf(reservationStatus.toUpperCase());
    }
}
