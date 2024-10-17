package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReservationDto(
        UUID id,
        TimeSlotDto timeSlotDto,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        LocalDate reservationDate,
        Integer reservationTime,
        Integer price,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        String deletedBy,
        LocalDateTime deletedAt
) {

    public static ReservationDto from(Reservation entity) {
        return ReservationDto.builder()
                .id(entity.getId())
                .timeSlotDto(TimeSlotDto.from(entity.getTimeSlot()))
                .customerId(entity.getCustomerId())
                .serviceProviderId(entity.getServiceProviderId())
                .reservationStatus(entity.getReservationStatus())
                .reservationDate(entity.getReservationDate())
                .reservationTime(entity.getReservationTime())
                .price(entity.getPrice())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedBy(entity.getUpdatedBy())
                .updatedAt(entity.getUpdatedAt())
                .deletedBy(entity.getDeletedBy())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
