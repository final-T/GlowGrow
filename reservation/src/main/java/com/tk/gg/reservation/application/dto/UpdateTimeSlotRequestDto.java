package com.tk.gg.reservation.application.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateTimeSlotRequestDto(
        Long serviceProviderId,
        LocalDate availableDate,
        Integer availableTime,
        Boolean isReserved
) {
}
