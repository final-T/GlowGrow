package com.tk.gg.reservation.infrastructure.messaging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "[GRADE-UP-PRODUCER]")
@RequiredArgsConstructor
public class ToUserKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendGradeForReservationEventToUser(GradeForReservationEventDto event) {
        log.info("예약에 대한 평가 정보 유저에게 발행: {}", event);
        kafkaTemplate.send("grade-reservation-user", event);
        log.info("평가정보 발행 완료");
    }

    public void sendGradeForReviewEventToUser(GradeForReviewEventDto event) {
        log.info("예약에 대한 평가 정보 유저에게 발행: {}", event);
        kafkaTemplate.send("grade-review-user", event);
        log.info("평가정보 발행 완료");
    }
}
