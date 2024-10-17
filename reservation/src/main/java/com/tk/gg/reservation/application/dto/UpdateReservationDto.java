package com.tk.gg.reservation.application.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record UpdateReservationDto(
        UUID timeSlotId,
        LocalDate reservationDate,
        Integer reservationTime,
        Integer price
) {
}
