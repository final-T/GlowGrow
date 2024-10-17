package com.tk.gg.users.infra.messaging;

import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.common.kafka.grade.GradeForReviewEventDto;
import com.tk.gg.users.application.service.GradeService;
import com.tk.gg.users.domain.service.GradeDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "GRADE-EVENT-LISTENER")
@RequiredArgsConstructor
public class GradeKafkaListener {

    private final GradeService gradeService;
    private final GradeDomainService gradeDomainService;

    // 예약 리스터
    @KafkaListener(topics = "grade-reservation", groupId = "grade-group")
    public void handleGradeForReview(GradeForReservationEventDto event) {
        gradeService.updateUserGradeByReservation(event);
    }

    // 리뷰 리스너
    @KafkaListener(topics = "grade-review", groupId = "grade-group")
    public void handleGradeForReview(GradeForReviewEventDto event) {
        gradeService.updateUserGradeByReview(event);
    }

    // 신고 리스너
}
