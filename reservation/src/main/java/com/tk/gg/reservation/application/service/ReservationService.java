package com.tk.gg.reservation.application.service;


import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CreateReservationDto;
import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import com.tk.gg.reservation.domain.service.TimeSlotDomainService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.reservation.infrastructure.messaging.GradeKafkaProducer;
import com.tk.gg.reservation.infrastructure.messaging.NotificationKafkaProducer;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationDomainService reservationDomainService;
    private final TimeSlotDomainService timeSlotDomainService;
    private final ReviewDomainService reviewDomainService;
    private final GradeKafkaProducer gradeKafkaProducer;
    private final NotificationKafkaProducer notificationKafkaProducer;


    @Transactional(readOnly = true)
    public Page<ReservationDto> searchReservations(
            LocalDate startDate, LocalDate endDate,
            ReservationStatus status, Pageable pageable, AuthUserInfo userInfo
    ) {
        return reservationDomainService.searchReservations(startDate, endDate, status, pageable, userInfo).map(ReservationDto::from);
    }

    @Transactional(readOnly = true)
    public ReservationDto getOneReservation(UUID reservationId, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if (!canHandleReservation(reservation, userInfo)) {
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        return ReservationDto.from(reservation);
    }

    @Transactional
    public ReservationDto createReservation(CreateReservationDto dto) {
        TimeSlot timeSlot = timeSlotDomainService.getOne(dto.timeSlotId());
        // 이미 예약된 슬롯은 예약 불가!
        if (timeSlot.getIsReserved()) throw new GlowGlowException(RESERVATION_ALREADY_EXIST);
        // 타임슬롯 시간정보와 일치하지 않으면 에러
        if (!dto.reservationDate().equals(timeSlot.getAvailableDate()) &&
                !dto.reservationTime().equals(timeSlot.getAvailableTime())
        ) {
            throw new GlowGlowException(RESERVATION_CREATE_FAILED);
        }
        Reservation reservation = reservationDomainService.create(dto, timeSlot);

        // 예약 접수 알림
        notificationKafkaProducer.sendReservationNotificationToUsers(
                dto.customerId(), dto.serviceProviderId(), "예약이 접수되었습니다."
        );

        return ReservationDto.from(reservation);
    }

    @Transactional
    public void updateReservation(UUID reservationId, UpdateReservationDto dto, AuthUserInfo userInfo) {
        TimeSlot timeSlot = timeSlotDomainService.getOne(dto.timeSlotId());
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if (!canHandleReservation(reservation, userInfo)) {
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        reservationDomainService.updateOne(reservation, dto, timeSlot);
    }

    @Transactional
    public void updateReservationStatus(UUID reservationId, ReservationStatus status, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if (!canHandleReservation(reservation, userInfo)) {
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        // 사용자 권한 status -> 취소만 가능
        if (userInfo.getUserRole().equals(UserRole.CUSTOMER) && !status.equals(ReservationStatus.CANCEL)){
            throw new GlowGlowException(RESERVATION_FORBIDDEN_STATUS);
        }

        // 예약 상태 변경
        reservationDomainService.updateStatus(reservation, status);

        // 만약 예약 상태가 성공적으로 DONE 이 된다면, Grade 생성
        if (status.equals(ReservationStatus.DONE)) {
            gradeKafkaProducer.sendReservationDoneEventForGrade(GradeForReservationEventDto.builder()
                    .reservationId(reservationId).customerId(reservation.getCustomerId())
                    .providerId(reservation.getServiceProviderId())
                    .build()
            );
        }

        // 예약 상태 변경 알림
        notificationKafkaProducer.sendReservationToNotificationEvent(KafkaNotificationDto.builder()
                .userId(reservation.getCustomerId()).message("예약이" + status.getDescription() + "되었습니다.")
                .type(NotificationType.RESERVATION.getName()).build()
        );
    }

    @Transactional
    public void deleteReservation(UUID reservationId, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if (!canHandleReservation(reservation, userInfo)) {
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        reservationDomainService.deleteOne(reservation, userInfo.getEmail());
    }

    private boolean canHandleReservation(Reservation reservation, AuthUserInfo userInfo) {
        if (userInfo.getUserRole().equals(UserRole.CUSTOMER)) {
            return reservation.getCustomerId().equals(userInfo.getId());
        } else if (userInfo.getUserRole().equals(UserRole.PROVIDER)) {
            return reservation.getServiceProviderId().equals(userInfo.getId());
        } else return userInfo.getUserRole().equals(UserRole.MASTER);
    }


    //TODO : 구현 중
//    @Scheduled(cron = "0 0 17 * * ?") // 매일 오후 5시
    public void notifyUsersForReviews() {
        List<Reservation> doneReservations = reservationDomainService.getReservationsByStatusIsDone();

        for (Reservation reservation : doneReservations) {
            // 리뷰가 존재하는지 확인
            boolean hasReview = reviewDomainService.existsByReservationId(reservation.getId());
            if (!hasReview) {
                // 리뷰를 작성하도록 사용자들에게 알람 이벤트 발행
                notificationKafkaProducer.sendReservationNotificationToUsers(
                        reservation.getCustomerId(),
                        reservation.getServiceProviderId(),
                        "서비스 완료가 된 예약에 대해 리뷰를 남길 수 있습니다!"
                );
            }
        }
    }
}
