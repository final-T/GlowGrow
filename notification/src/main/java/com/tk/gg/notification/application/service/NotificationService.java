package com.tk.gg.notification.application.service;

import com.tk.gg.notification.domain.service.NotificationDomainService;
import com.tk.gg.notification.presentation.response.NotificationResponse;
import com.tk.gg.security.user.AuthUserInfoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationDomainService notificationDomainService;
    private final UserService userService;

    public Page<NotificationResponse> getNotifications(AuthUserInfoImpl authUserInfo, Pageable pageable) {
        userService.checkUserExists(authUserInfo.getEmail());

        return notificationDomainService.getNotifications(authUserInfo.getId(), pageable).map(NotificationResponse::from);
    }

    public void readNotification(AuthUserInfoImpl authUserInfo, UUID notificationId) {
        userService.checkUserExists(authUserInfo.getEmail());

        notificationDomainService.readNotification(authUserInfo, notificationId);
    }
}
