package com.tk.gg.notification.domain.service;

import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import com.tk.gg.notification.application.dto.NotificationDto;
import com.tk.gg.notification.domain.model.Notification;
import com.tk.gg.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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
        return notificationRepository.findAllByUserId(userId, pageable).map(NotificationDto::from);
    }
}
