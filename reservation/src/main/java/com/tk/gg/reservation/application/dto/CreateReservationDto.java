package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CreateReservationDto(
        UUID timeSlotId,
        Long customerId,
        Long serviceProviderId,
        LocalDate reservationDate,
        Integer reservationTime,
        Integer price
) {

    public Reservation toEntity(TimeSlot timeSlot){
        return Reservation.builder()
                .timeSlot(timeSlot)
                .customerId(customerId)
                .serviceProviderId(serviceProviderId)
                .reservationDate(reservationDate)
                .reservationTime(reservationTime)
                .reservationStatus(ReservationStatus.CHECK)
                .price(price)
                .build();
    }
}
