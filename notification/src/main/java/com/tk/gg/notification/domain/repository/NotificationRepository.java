package com.tk.gg.notification.domain.repository;

import com.tk.gg.notification.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
