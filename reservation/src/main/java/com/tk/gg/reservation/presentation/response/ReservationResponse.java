package com.tk.gg.reservation.presentation.response;

import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ReservationResponse(
        UUID id,
        TimeSlot timeSlot,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        LocalDate reservationDate,
        Integer reservationTime
) {
    public static ReservationResponse from(ReservationDto dto) {
        return ReservationResponse.builder()
                .id(dto.id())
                .timeSlot(dto.timeSlot())
                .customerId(dto.customerId())
                .serviceProviderId(dto.serviceProviderId())
                .reservationStatus(dto.reservationStatus())
                .reservationDate(dto.reservationDate())
                .reservationTime(dto.reservationTime())
                .build();
    }
}
