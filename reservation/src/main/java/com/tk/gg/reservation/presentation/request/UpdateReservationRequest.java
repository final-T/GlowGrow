package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateReservationRequest(
        @NotNull(message = "예약타임슬롯 ID 는 필수입니다.")
        UUID timeSlotId,

        @NotNull(message = "예약 고객 ID 는 필수입니다.")
        Long customerId,

        @NotNull(message = "예약에 대한 서비스 제공자 ID 는 필수입니다.")
        Long serviceProviderId,

        @NotNull(message = "예약 상태는 필수입니다.")
        ReservationStatus reservationStatus,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate reservationDate,

        @NotNull(message = "예약 시간은 필수입니다.")
        @Min(value = 1, message = "예약 가능 시간은 1~24 입니다.")
        @Max(value = 24, message = "예약 가능 시간은 1~24 입니다.")
        Integer reservationTime,

        @NotNull(message = "예약 시간은 필수입니다.")
        @Min(value = 0, message = "가격은 0이상 입니다.")
        Integer price

) {

    public UpdateReservationDto toDto() {
        return UpdateReservationDto.builder()
                .timeSlotId(timeSlotId)
                .customerId(customerId)
                .serviceProviderId(serviceProviderId)
                .reservationStatus(reservationStatus)
                .reservationDate(reservationDate)
                .reservationTime(reservationTime)
                .price(price)
                .build();
    }
}
