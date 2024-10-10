package com.tk.gg.notification.presentation.response;

import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.notification.application.dto.NotificationDto;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record NotificationResponse(
        UUID notificationId,
        String message,
        NotificationType notificationType,
        Boolean isRead,
        LocalDateTime readAt
) {
    public static NotificationResponse from(NotificationDto dto) {
        return NotificationResponse.builder()
                .notificationId(dto.notificationId())
                .message(dto.message())
                .notificationType(dto.notificationType())
                .isRead(dto.isRead())
                .readAt(dto.readAt() == null ? null : dto.readAt())
                .build();
    }
}
