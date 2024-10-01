package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.CreateReservationDto;

import java.time.LocalDate;
import java.util.UUID;

public record CreateReservationRequest(
        UUID timeSlotId,
        Long customerId,
        Long serviceProviderId,
        LocalDate reservationDate,
        Integer reservationTime
) {
    public CreateReservationDto toDto(){
        return CreateReservationDto.builder()
                .timeSlotId(timeSlotId)
                .customerId(customerId)
                .serviceProviderId(serviceProviderId)
                .reservationDate(reservationDate)
                .reservationTime(reservationTime)
                .build();
    }
}
