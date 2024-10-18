package com.tk.gg.reservation.application.service;

import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import com.tk.gg.reservation.infrastructure.messaging.NotificationKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewNotifyScheduler {
    private final ReservationDomainService reservationDomainService;
    private final ReviewDomainService reviewDomainService;
    private final NotificationKafkaProducer notificationKafkaProducer;


    //TODO : 테스트 중
//    @Scheduled(cron = "0 0 22 * * ?") // 매일 오후 10시
    public void notifyUsersForReviews() {
        int batchSize = 100; // 배치 크기 설정
        int offset = 0;
        List<Reservation> doneReservations;
        do {
            doneReservations = reservationDomainService.getReservationsByStatusIsDoneWithLimit(offset, batchSize);
            for (Reservation reservation : doneReservations) {
                // 리뷰가 존재하는지 확인
                List<Review> reviews = reviewDomainService.getReviewsByReservationId(reservation.getId());
                if (reviews.isEmpty()) {
                    notificationKafkaProducer.sendReservationNotificationToUsers(
                            reservation.getCustomerId(),
                            reservation.getServiceProviderId(),
                            "서비스 완료가 된 예약에 대해 리뷰를 남길 수 있습니다!"
                    );
                }else if(reviews.size() == 1) {
                    notificationKafkaProducer.sendReservationToNotificationEvent(
                            KafkaNotificationDto.builder().type(NotificationType.RESERVATION.getName())
                                    .userId(reviews.get(0).getTargetUserId())
                                    .message("서비스 완료가 된 예약에 대해 리뷰를 남길 수 있습니다!")
                                    .build()
                    );
                }
            }
            offset += batchSize; // 배치 처리 후 다음으로 이동
        } while(doneReservations.size() == batchSize);
    }
}
