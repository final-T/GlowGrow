package com.tk.gg.reservation.infrastructure.messaging;


import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.common.kafka.grade.GradeForReviewEventDto;
import com.tk.gg.reservation.application.dto.GradeDto;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.service.GradeDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "GRADE-EVENT-LISTENER")
@RequiredArgsConstructor
public class GradeKafkaListener {
    private final GradeDomainService gradeDomainService;
    // TODO : 분리 고민...
    private final ReviewDomainService reviewDomainService;
    //    private final ReviewDomainService reviewDomainService;
    private final GradeKafkaProducer kafkaProducer;

    // 예약 완료 -> 평가정보 반영 리스너 -> 이벤트 발행
    @KafkaListener(topics = "grade-reservation", groupId = "grade-group")
    public void handleGradeForReservation(GradeForReservationEventDto event) {
        log.info("예약에 대한 평가반영 이벤트 수신 및 저장: {}", event);
        //TODO : 예약 검증?
        gradeDomainService.createGradeForReservation(event);
        log.info("예약 -> 평가정보 저장 완료");
        kafkaProducer.sendGradeForReservationEventToUser(event);
        log.info("유저에게 평가정보 업데이트 송신 완료");
    }

    // 리뷰 및 평점 작성 -> 평가정보 반영 리스너 -> 이벤트 발행
    @KafkaListener(topics = "grade-review", groupId = "grade-group")
    public void handleGradeForReview(GradeDto event) {
        log.info("리뷰에 대한 평가반영 이벤트 수신 및 저장: {}", event);
        gradeDomainService.updateGradeForReview(event);
        log.info("리뷰 -> 평가정보 저장 완료");
        //이벤트 발행을 위한 리뷰 조회
        //TODO : 로직 분리 고민...다론 도메인 엔티티가 여기까지?
        Review review = reviewDomainService.getOne(event.reviewId());
        //TODO : 어차피 보내고 유저 모듈에서 FeignClient 로 조회한다면 gradeId 나 userId 만 있어도 충분하지 않나
        GradeForReviewEventDto toUserEvent = GradeForReviewEventDto.builder()
                .reviewId(review.getId())
                .reviewerId(review.getReviewerId())
                .targetUserId(review.getTargetUserId())
                .userType(event.userType())
                .build();
        kafkaProducer.sendGradeForReviewEventToUser(toUserEvent);
        log.info("유저에게 평가정보 업데이트 송신 완료");
    }
}
