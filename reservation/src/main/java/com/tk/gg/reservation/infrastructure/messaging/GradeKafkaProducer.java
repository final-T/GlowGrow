package com.tk.gg.reservation.infrastructure.messaging;


import com.tk.gg.common.kafka.grade.GradeForReportEventDto;
import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.common.kafka.grade.GradeForReviewEventDto;
import com.tk.gg.reservation.application.dto.GradeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//TODO: retry, recover 로직 필요?
@Component
@Slf4j(topic = "[GRADE-UP-PRODUCER]")
@RequiredArgsConstructor
public class GradeKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendGradeForReservationEventToUser(GradeForReservationEventDto event) {
        log.info("예약에 대한 평가 정보 유저에게 발행: {}", event);
        kafkaTemplate.send("grade-reservation-user", event);
        log.info("평가정보 발행 완료");
    }

    public void sendGradeForReviewEventToUser(GradeForReviewEventDto event) {
        log.info("리뷰에 대한 평가 정보 유저에게 발행: {}", event);
        kafkaTemplate.send("grade-review-user", event);
        log.info("평가정보 발행 완료");
    }

    public void sendGradeEventForReportToUser(GradeForReportEventDto event){
        log.info("리뷰에 대한 평가 정보 유저에게 발행: {}", event);
        kafkaTemplate.send("grade-report-user", event);
        log.info("평가정보 발행 완료");
    }



    public void sendReservationDoneEventForGrade(GradeForReservationEventDto event) {
        log.info("예약 완료에 대한 평가정보 생성 발행: {}", event);
        kafkaTemplate.send("grade-reservation", event);
        log.info("평가정보 발행 완료");
    }

    public void sendReviewEventForGrade(GradeDto dto) {
        log.info("리뷰에 포함된 평가정보 발행 : {}", dto);
        kafkaTemplate.send("grade-review", dto);
        log.info("평가정보 발행 완료");
    }
}
