package com.tk.gg.reservation.presentation.request;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.application.dto.CreateReportDto;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateReportRequest(
        @NotNull(message = "관련 예약 ID 는 필수입니다.")
        UUID reservationId,

        @NotNull(message = "신고자 ID 는 필수입니다.")
        Long userId,

        @NotNull(message = "신고 대상자 ID 는 필수입니다.")
        Long targetUserId,

        @NotBlank(message = "신고 이유는 필수입니다.")
        @Size(min = 10, max = 255, message = "신고 내용은 최소 10자에서 최대 255자까지 가능합니다.")
        String reason
) {

    public CreateReportDto toDto() {
        return CreateReportDto.builder()
                .reservationId(reservationId)
                .userId(userId)
                .targetUserId(targetUserId)
                .reason(reason)
                .build();
    }

}
