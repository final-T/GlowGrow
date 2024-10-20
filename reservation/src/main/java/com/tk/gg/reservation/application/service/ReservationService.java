package com.tk.gg.reservation.application.service;


import com.tk.gg.common.enums.NotificationType;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.kafka.alarm.KafkaNotificationDto;
import com.tk.gg.common.kafka.payment.PaymentReservationResponseDto;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CreateReservationDto;
import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.TimeSlotDomainService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.reservation.infrastructure.messaging.GradeKafkaProducer;
import com.tk.gg.reservation.infrastructure.messaging.NotificationKafkaProducer;
import com.tk.gg.reservation.infrastructure.messaging.PaymentKafkaProducer;
import com.tk.gg.reservation.presentation.request.ReservationSearchCondition;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;
import static com.tk.gg.reservation.application.util.ReservationUserCheck.*;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationDomainService reservationDomainService;
    private final TimeSlotDomainService timeSlotDomainService;
    private final GradeKafkaProducer gradeKafkaProducer;
    private final NotificationKafkaProducer notificationKafkaProducer;
    private final PaymentKafkaProducer paymentKafkaProducer;


    @Transactional(readOnly = true)
    public Page<ReservationDto> searchReservations(
            ReservationSearchCondition searchCondition, Pageable pageable, AuthUserInfo userInfo
    ) {
        return reservationDomainService.searchReservations(searchCondition, pageable, userInfo).map(ReservationDto::from);
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
        // 현재 시간보다 이전의 예약일 경우 예외 처리
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간
        LocalDateTime reservationDateTime = LocalDateTime.of(dto.reservationDate(),
                dto.reservationTime() == 24 ? LocalTime.MIDNIGHT : LocalTime.of(dto.reservationTime(), 0)
        );
        if (reservationDateTime.isBefore(now)) {
            throw new GlowGlowException(RESERVATION_BEFORE_NOW);
        }

        TimeSlot timeSlot = timeSlotDomainService.getOne(dto.timeSlotId());
        // 이미 예약된 슬롯은 예약 불가!
        if (timeSlot.getIsReserved()) throw new GlowGlowException(RESERVATION_ALREADY_EXIST);
        // 타임슬롯 시간정보와 일치하지 않으면 에러
        if (!dto.reservationDate().equals(timeSlot.getAvailableDate()) ||
                !dto.reservationTime().equals(timeSlot.getAvailableTime())
        ) {
            throw new GlowGlowException(RESERVATION_WRONG_TIME);
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
        // 사용자 권한 status -> 취소, 결제 요청만 가능
        if (userInfo.getUserRole().equals(UserRole.CUSTOMER)) {
            if (!status.equals(ReservationStatus.CANCEL) && !status.equals(ReservationStatus.PAYMENT_CALL)) {
                throw new GlowGlowException(RESERVATION_FORBIDDEN_STATUS);
            }
        }

        // 결제 요청 시 카프카 이벤트 발행
        if (status.equals(ReservationStatus.PAYMENT_CALL)) {
            if (!reservation.getReservationStatus().equals(ReservationStatus.DONE)) {
                throw new GlowGlowException(GlowGlowError.RESERVATION_NOT_DONE_FOR_PAYMENT);
            }
            paymentKafkaProducer.sendReservationToPaymentEvent(new PaymentReservationResponseDto(
                    reservationId, reservation.getCustomerId(), reservation.getServiceProviderId(), reservation.getPrice()
            ));
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
}
