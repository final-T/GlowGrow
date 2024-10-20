package com.tk.gg.reservation.infrastructure.messaging;


import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "[Reservation-noti-producer]")
@RequiredArgsConstructor
public class NotificationKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendReportToNotificationEvent(KafkaNotificationDto event) {
        log.info("신고 접수 시 알림 이벤트 발행: {}", event);
        kafkaTemplate.send("noti-send", event);
        log.info("신고에 대한 알림 이벤트 발행 완료");
    }


    public void sendReservationToNotificationEvent(KafkaNotificationDto event) {
        log.info("예약 관련 알림 이벤트 발행: {}", event);
        kafkaTemplate.send("noti-send", event);
        log.info("알림 이벤트 발행 완료");
    }

    public void sendReservationNotificationToUsers(Long customerId, Long providerId, String message) {
        KafkaNotificationDto notificationDtoForCustomer = KafkaNotificationDto.builder()
                .userId(customerId)
                .message(message)
                .type(NotificationType.RESERVATION.getName()).build();
        KafkaNotificationDto notificationDtoForProvider = KafkaNotificationDto.builder()
                .userId(providerId)
                .message(message)
                .type(NotificationType.RESERVATION.getName()).build();

        this.sendReservationToNotificationEvent(notificationDtoForCustomer);
        this.sendReservationToNotificationEvent(notificationDtoForProvider);
    }
}
