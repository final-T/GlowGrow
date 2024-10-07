package com.tk.gg.reservation.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ReservationResponse(
        UUID id,
        TimeSlotResponse timeSlot,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate reservationDate,
        Integer reservationTime
) {
    public static ReservationResponse from(ReservationDto dto) {
        return ReservationResponse.builder()
                .id(dto.id())
                .timeSlot(TimeSlotResponse.from(dto.timeSlotDto()))
                .customerId(dto.customerId())
                .serviceProviderId(dto.serviceProviderId())
                .reservationStatus(dto.reservationStatus())
                .reservationDate(dto.reservationDate())
                .reservationTime(dto.reservationTime())
                .build();
    }
}
