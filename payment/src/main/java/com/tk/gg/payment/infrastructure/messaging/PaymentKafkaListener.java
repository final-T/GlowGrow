package com.tk.gg.payment.infrastructure.messaging;

import com.tk.gg.common.kafka.payment.PaymentReservationResponseDto;
import com.tk.gg.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "PAYMENT-EVENT-LISTENER")
@RequiredArgsConstructor
public class PaymentKafkaListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "reservation-payment", groupId = "payment-service")
    public void listenPaymentRequest(PaymentReservationResponseDto event) {
        log.info("Received payment request event: {}", event);
        paymentService.savePendingPaymentRequest(event);
        log.info("Payment request saved successfully");
    }

}
