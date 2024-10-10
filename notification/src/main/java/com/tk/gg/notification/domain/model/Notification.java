package com.tk.gg.notification.domain.model;

import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "p_notifications")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id", updatable = false, nullable = false)
    private UUID notificationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public static Notification create(KafkaNotificationDto dto){
        return Notification.builder()
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .type(NotificationType.valueOf(dto.getType()))
                .isRead(false)
                .isDeleted(false)
                .build();
    }

    public void readNotification(String username){
        this.isRead = true;
        this.readAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = username;
    }
}
