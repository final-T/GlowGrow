package com.tk.gg.reservation.infrastructure.messaging;


import com.tk.gg.common.kafka.payment.PaymentForReservationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "[Reservation-payment-producer]")
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendReservationToPaymentEvent(PaymentForReservationEventDto event) {
        log.info("완료된 예약에 대해 결제 요청 이벤트 발행: {}", event);
        kafkaTemplate.send("reservation-payment", event);
        log.info("신고에 대한 알림 이벤트 발행 완료");
    }
}
