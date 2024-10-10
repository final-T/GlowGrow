package com.tk.gg.notification.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.notification.application.service.NotificationService;
import com.tk.gg.notification.presentation.response.NotificationResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.NOTIFICATION_READ_SUCCESS;
import static com.tk.gg.common.response.ResponseMessage.NOTIFICATION_RETRIEVE_SUCCESS;

@RestController("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public GlobalResponse<Page<NotificationResponse>> getNotifications(
            @AuthUser AuthUserInfoImpl authUserInfo,
            @RequestParam(value = "isRead", required = false) Boolean isRead,
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC)Pageable pageable
    ) {
        return ApiUtils.success(NOTIFICATION_RETRIEVE_SUCCESS.getMessage(), notificationService.getNotifications(authUserInfo,isRead, pageable));
    }

    @PostMapping("/{notificationId}")
    public GlobalResponse<String> readNotification(
            @AuthUser AuthUserInfoImpl authUserInfo,
            @PathVariable("notificationId") UUID notificationId
    ) {
        notificationService.readNotification(authUserInfo, notificationId);
        return ApiUtils.success(NOTIFICATION_READ_SUCCESS.getMessage());
    }
}
