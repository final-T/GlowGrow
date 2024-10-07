package com.tk.gg.notification.domain.service;

import com.tk.gg.common.kafka.alarm.NotificationDto;
import com.tk.gg.notification.domain.model.Notification;
import com.tk.gg.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationDomainService {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "noti-send", groupId = "noti-send-group")
    public void alterListen(NotificationDto notificationDto) {

        Notification notification = Notification.create(notificationDto);
        notificationRepository.save(notification);

        //TODO : 알림 저장 이력 만들기
    }


}
