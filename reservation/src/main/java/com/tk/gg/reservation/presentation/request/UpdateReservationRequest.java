package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.type.ReservationStatus;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateReservationRequest(
        UUID timeSlotId,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        LocalDate reservationDate,
        Integer reservationTime
) {

    public UpdateReservationDto toDto() {
        return UpdateReservationDto.builder()
                .timeSlotId(timeSlotId)
                .customerId(customerId)
                .serviceProviderId(serviceProviderId)
                .reservationStatus(reservationStatus)
                .reservationDate(reservationDate)
                .reservationTime(reservationTime)
                .build();
    }
}
