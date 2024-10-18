package com.tk.gg.reservation.application.service;

import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import com.tk.gg.reservation.ReservationApplication;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.infrastructure.config.SwaggerConfig;
import com.tk.gg.reservation.infrastructure.messaging.NotificationKafkaProducer;
import com.tk.gg.reservation.infrastructure.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ReservationApplication.class)
@ActiveProfiles("test") // test 프로파일 사용
@EmbeddedKafka(partitions = 1, topics = {"noti-send"})
class ReviewNotifySchedulerTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReviewDomainService reviewDomainService;
    @Autowired
    private ReservationDomainService reservationDomainService;
    @Autowired
    private NotificationKafkaProducer notificationKafkaProducer;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 생성
        List<Reservation> reservations = new ArrayList<>();
        IntStream.range(0, 500).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .customerId((long) (i + 1))
                    .serviceProviderId((long) ((i % 100) + 1))
                    .reservationStatus(ReservationStatus.DONE)
                    .reservationDate(LocalDate.now())
                    .reservationTime(17)
                    .price(500)
                    .build();
            reservations.add(reservation);
        });
        reservationRepository.saveAll(reservations);
    }

    @Test
    public void notifyUsersForReviewsTest() {
        // 성능 테스트 시작 시간
        long startTime = System.currentTimeMillis();

        // 스케줄링 메서드 호출
        notifyUsersForReviews();

        // 성능 테스트 종료 시간
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        System.out.println("Execution Time: " + duration + "ms");
    }

    private void notifyUsersForReviews() {
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
            offset += batchSize;
        } while(doneReservations.size() == batchSize);
    }
}