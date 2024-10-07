package com.tk.gg.reservation.application.dto;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.domain.model.Report;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReportDto(
        UUID id,
        UUID reservationId,
        Long customerId,
        Long serviceProviderId,
        ReservationStatus reservationStatus,
        LocalDate reservationDate,
        Integer reservationTime,
        Long userId,
        Long targetUserId,
        UserRole reporterType,
        String reason,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        String deletedBy,
        LocalDateTime deletedAt
) {

    public static ReportDto from(Report report) {
        return ReportDto.builder()
                .id(report.getId())
                .reservationId(report.getReservation().getId())
                .customerId(report.getReservation().getCustomerId())
                .serviceProviderId(report.getReservation().getServiceProviderId())
                .reservationStatus(report.getReservation().getReservationStatus())
                .reservationDate(report.getReservation().getReservationDate())
                .reservationTime(report.getReservation().getReservationTime())
                .userId(report.getUserId())
                .targetUserId(report.getTargetUserId())
                .reporterType(report.getReporterType())
                .reason(report.getReason())
                .createdBy(report.getCreatedBy())
                .createdAt(report.getCreatedAt())
                .updatedBy(report.getUpdatedBy())
                .updatedAt(report.getUpdatedAt())
                .deletedBy(report.getDeletedBy())
                .deletedAt(report.getDeletedAt())
                .build();
    }
}
