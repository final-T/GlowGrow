package com.tk.gg.notification.domain.service;

import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.notification.application.dto.NotificationDto;
import com.tk.gg.notification.domain.model.Notification;
import com.tk.gg.notification.domain.repository.NotificationRepository;
import com.tk.gg.security.user.AuthUserInfoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.NOTIFICATION_ALREADY_READ;
import static com.tk.gg.common.response.exception.GlowGlowError.NOTIFICATION_NO_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationDomainService {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "noti-send", groupId = "noti-send-group")
    public void alterListen(KafkaNotificationDto kafkaNotificationDto) {

        Notification notification = Notification.create(kafkaNotificationDto);
        notificationRepository.save(notification);

        //TODO : 알림 저장 이력 만들기
    }


    public Page<NotificationDto> getNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUserIdAndIsDeletedFalse(userId, pageable).map(NotificationDto::from);
    }

    public void readNotification(AuthUserInfoImpl authUserInfo, UUID notificationId) {
        Notification notification = notificationRepository.findByNotificationIdAndUserIdAndIsDeletedFalse(notificationId, authUserInfo.getId())
                .orElseThrow(() -> new GlowGlowException(NOTIFICATION_NO_EXIST));

        if(notification.getIsRead())
            throw new GlowGlowException(NOTIFICATION_ALREADY_READ);

        notification.readNotification(authUserInfo.getUsername());
    }
}
