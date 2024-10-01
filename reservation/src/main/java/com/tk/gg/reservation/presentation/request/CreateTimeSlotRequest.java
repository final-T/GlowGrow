package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record CreateTimeSlotRequest(
        @NotNull(message = "서비스 제공자 ID 는 필수입니다.")
        Long serviceProviderId,

        @NotNull(message = "예약 가능 날짜는 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
        LocalDate availableDate,

        @NotNull(message = "예약 가능 시간은 필수입니다.")
        @Min(value = 1, message = "예약 가능 시간은 1~24 입니다.")
        @Max(value = 24, message = "예약 가능 시간은 1~24 입니다.")
        Integer availableTime
) {
    public CreateTimeSlotRequestDto toDto() {
        return CreateTimeSlotRequestDto.builder()
                .serviceProviderId(serviceProviderId)
                .availableDate(availableDate)
                .availableTime(availableTime)
                .build();
    }
}
