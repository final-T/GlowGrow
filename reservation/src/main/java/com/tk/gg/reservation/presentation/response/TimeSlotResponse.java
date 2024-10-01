package com.tk.gg.reservation.presentation.response;


import com.tk.gg.reservation.application.dto.TimeSlotDto;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record TimeSlotResponse(
        UUID id,
        Long serviceProviderId,
        Date availableDate,
        Integer availableTime,
        Boolean isReserved
) {

    public static TimeSlotResponse from(TimeSlotDto dto) {
        return TimeSlotResponse.builder()
                .id(dto.id())
                .serviceProviderId(dto.serviceProviderId())
                .availableDate(dto.availableDate())
                .availableTime(dto.availableTime())
                .isReserved(dto.isReserved())
                .build();
    }
}
