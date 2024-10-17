package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record UpdateReservationDto(
        UUID timeSlotId,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        LocalDate reservationDate,
        Integer reservationTime,
        Integer price
) {
}
