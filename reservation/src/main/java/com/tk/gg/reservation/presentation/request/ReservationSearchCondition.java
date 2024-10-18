package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.domain.type.ReservationStatus;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ReservationSearchCondition(
        @Parameter(name = "startDate", description = "검색 시작 범위 날짜(yyyy-MM-dd)")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Parameter(name = "endDate", description = "검색 끝 범위 날짜(yyyy-MM-dd)")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @Parameter(name = "status", description = "예약 상태")
        ReservationStatus status,

        @Parameter(name = "customerId", description = "고객 ID")
        Long customerId,

        @Parameter(name = "providerId", description = "서비스 제공자 ID")
        Long providerId
) {
}
