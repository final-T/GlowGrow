package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;

import java.util.Date;

//TODO : validation 추가
public record CreateTimeSlotRequest(
        Long serviceProviderId,
        Date availableDate,
        Integer availableTime,
        Boolean isReserved
) {
    public CreateTimeSlotRequestDto toDto() {
        return CreateTimeSlotRequestDto.builder()
                .serviceProviderId(serviceProviderId)
                .availableDate(availableDate)
                .availableTime(availableTime)
                .isReserved(isReserved)
                .build();
    }
}
