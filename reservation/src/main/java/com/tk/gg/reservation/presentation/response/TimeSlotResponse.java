package com.tk.gg.reservation.presentation.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tk.gg.reservation.application.dto.TimeSlotDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record TimeSlotResponse(
        UUID id,
        Long serviceProviderId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate availableDate,
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
