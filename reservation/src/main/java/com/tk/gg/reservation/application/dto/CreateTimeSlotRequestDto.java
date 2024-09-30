package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.TimeSlot;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Date;

@Builder
public record CreateTimeSlotRequestDto(
        Long serviceProviderId,
        Date availableDate,
        Integer availableTime,
        Boolean isReserved
) {

    public TimeSlot toEntity() {
        return TimeSlot.builder()
                .serviceProviderId(serviceProviderId)
                .availableDate(availableDate)
                .availableTime(availableTime)
                .isReserved(isReserved)
                .build();
    }
}
