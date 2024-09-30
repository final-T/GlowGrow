package com.tk.gg.reservation.application.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record UpdateTimeSlotRequestDto(
        Long serviceProviderId,
        Date availableDate,
        Integer availableTime,
        Boolean isReserved
) {
}
