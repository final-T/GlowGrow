package com.tk.gg.payment.infrastructure.messaging;

import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "[Payment-noti-producer]")
@RequiredArgsConstructor
public class NotificationPaymentKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccessToNotificationEvent(KafkaNotificationDto event){
        log.info("결제 완료 알림 이벤트 발행 : {}", event);
        kafkaTemplate.send("noti-send",event);
        log.info("결제 완료 알림 이벤트 발행 완료");
    }

    public void sendPaymentFailToNotificationEvent(KafkaNotificationDto event){
        log.info("결제 실패 완료 이벤트 발행 : {}" ,event);
        kafkaTemplate.send("noti-send",event);
        log.info("결제 실패 알림 이벤트 발행 완료");
    }

    public void sendPaymentNotificationToUser(Long customerId, String message, boolean isSuccess){
        KafkaNotificationDto notificationDto = KafkaNotificationDto.builder()
                .userId(customerId)
                .message(message)
                .type(NotificationType.PAYMENT.getName())
                .build();

        if (isSuccess) {
            sendPaymentSuccessToNotificationEvent(notificationDto);
        } else {
            sendPaymentFailToNotificationEvent(notificationDto);
        }
    }

}
