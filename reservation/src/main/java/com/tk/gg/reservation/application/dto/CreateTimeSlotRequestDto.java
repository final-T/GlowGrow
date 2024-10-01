package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.TimeSlot;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateTimeSlotRequestDto(
        Long serviceProviderId,
        LocalDate availableDate,
        Integer availableTime
) {
    public TimeSlot toEntity() {
        return TimeSlot.builder()
                .serviceProviderId(serviceProviderId)
                .availableDate(availableDate)
                .availableTime(availableTime)
                .build();
    }
}
