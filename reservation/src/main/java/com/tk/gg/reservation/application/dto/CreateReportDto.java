package com.tk.gg.reservation.application.dto;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.domain.model.Report;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateReportDto(
        UUID reservationId,
        Long userId,
        Long targetUserId,
        String reason
) {

    public Report toEntity(Reservation reservation, UserRole reporterType) {
        return Report.builder()
                .id(reservationId)
                .reservation(reservation)
                .userId(userId)
                .targetUserId(targetUserId)
                .reporterType(reporterType)
                .reason(reason)
                .build();
    }
}
