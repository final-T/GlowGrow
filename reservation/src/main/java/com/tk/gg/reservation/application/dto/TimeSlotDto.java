package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.TimeSlot;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TimeSlotDto(
        UUID id,
        Long serviceProviderId,
        LocalDate availableDate,
        Integer availableTime,
        Boolean isReserved,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        String deletedBy,
        LocalDateTime deletedAt
) {
    public static TimeSlotDto from(TimeSlot timeSlot) {
        return TimeSlotDto.builder()
                .id(timeSlot.getId())
                .serviceProviderId(timeSlot.getServiceProviderId())
                .availableDate(timeSlot.getAvailableDate())
                .availableTime(timeSlot.getAvailableTime())
                .isReserved(timeSlot.getIsReserved())
                .createdBy(timeSlot.getCreatedBy())
                .createdAt(timeSlot.getCreatedAt())
                .updatedBy(timeSlot.getUpdatedBy())
                .updatedAt(timeSlot.getUpdatedAt())
                .deletedBy(timeSlot.getDeletedBy())
                .deletedAt(timeSlot.getDeletedAt())
                .build();
    }
}
