package com.tk.gg.notification.application.dto;

import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.notification.domain.model.Notification;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record NotificationDto(
        UUID notificationId,
        Long userId,
        String message,
        NotificationType notificationType,
        Boolean isRead,
        LocalDateTime readAt,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static NotificationDto from(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .isDeleted(notification.getIsDeleted())
                .createdAt(notification.getCreatedAt())
                .createdBy(notification.getCreatedBy())
                .updatedAt(notification.getUpdatedAt())
                .updatedBy(notification.getUpdatedBy())
                .deletedAt(notification.getDeletedAt())
                .deletedBy(notification.getDeletedBy())
                .build();
    }
}
