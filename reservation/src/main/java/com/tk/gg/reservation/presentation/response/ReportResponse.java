package com.tk.gg.reservation.presentation.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tk.gg.reservation.application.dto.ReportDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReportResponse(
        UUID id,
        UUID reservationId,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate reservationDate,
        Integer reservationTime,
        Long userId,
        Long targetUserId,
        String reason,
        String createdBy,
        LocalDateTime createdAt
) {

    public static ReportResponse from(ReportDto reportDto) {
        return ReportResponse.builder()
                .id(reportDto.id())
                .reservationId(reportDto.reservationId())
                .customerId(reportDto.customerId())
                .serviceProviderId(reportDto.serviceProviderId())
                .reservationStatus(reportDto.reservationStatus())
                .reservationDate(reportDto.reservationDate())
                .reservationTime(reportDto.reservationTime())
                .userId(reportDto.userId())
                .targetUserId(reportDto.targetUserId())
                .reason(reportDto.reason())
                .createdBy(reportDto.createdBy())
                .createdAt(reportDto.createdAt())
                .build();
    }
}
