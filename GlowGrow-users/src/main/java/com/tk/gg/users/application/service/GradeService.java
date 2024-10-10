package com.tk.gg.users.application.service;

import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.common.kafka.grade.GradeForReviewEventDto;
import com.tk.gg.users.domain.service.GradeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeDomainService gradeDomainService;
    private final ReservationService reservationService;

    public void updateUserGradeByReservation(GradeForReservationEventDto event) {
        // 고객 확인
        gradeDomainService.updateUserGradeByReservation(
                reservationService.getGradeForReservation(event.customerId(), event.reservationId())
        );

        // 디자이너 확인
        gradeDomainService.updateUserGradeByReservation(
                reservationService.getGradeForReservation(event.providerId(), event.reservationId())
        );
    }

    public void updateUserGradeByReview(GradeForReviewEventDto event) {
        gradeDomainService.updateUserGradeByReview(
                reservationService.getGradeForReview(event.targetUserId(), event.reviewId())
        );
    }
}
