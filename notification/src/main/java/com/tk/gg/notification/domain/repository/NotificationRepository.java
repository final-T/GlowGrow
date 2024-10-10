package com.tk.gg.notification.domain.repository;

import com.tk.gg.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAllByUserIdAndIsReadAndIsDeletedFalse(Long userId, Boolean isRead, Pageable pageable);
    Optional<Notification> findByNotificationIdAndUserIdAndIsDeletedFalse(UUID notificationId, Long userId);
}
