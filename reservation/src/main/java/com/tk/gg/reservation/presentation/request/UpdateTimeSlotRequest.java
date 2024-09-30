package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;

import java.util.Date;

//TODO : validation 추가
public record UpdateTimeSlotRequest(
        Long serviceProviderId,
        Date availableDate,
        Integer availableTime,
        Boolean isReserved
) {

    public UpdateTimeSlotRequestDto toDto() {
        return UpdateTimeSlotRequestDto.builder()
                .serviceProviderId(serviceProviderId)
                .availableDate(availableDate)
                .availableTime(availableTime)
                .isReserved(isReserved)
                .build();
    }
}
