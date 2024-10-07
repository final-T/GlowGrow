package com.tk.gg.reservation.application.service;


import com.tk.gg.common.enums.UserRole;
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
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationDomainService reservationDomainService;
    private final TimeSlotDomainService timeSlotDomainService;

    @Transactional(readOnly = true)
    public Page<ReservationDto> searchReservations(LocalDate startDate, LocalDate endDate, ReservationStatus status, Pageable pageable) {
        return reservationDomainService.searchReservations(startDate, endDate, status, pageable).map(ReservationDto::from);
    }

    @Transactional(readOnly = true)
    public ReservationDto getOneReservation(UUID reservationId) {
        return ReservationDto.from(reservationDomainService.getOne(reservationId));
    }

    @Transactional
    public ReservationDto createReservation(CreateReservationDto dto) {
        TimeSlot timeSlot = timeSlotDomainService.getOne(dto.timeSlotId());
        if (!dto.reservationDate().equals(timeSlot.getAvailableDate()) &&
                !dto.reservationTime().equals(timeSlot.getAvailableTime())
        ){
            throw new GlowGlowException(RESERVATION_CREATE_FAILED);
        }

        return ReservationDto.from(reservationDomainService.create(dto, timeSlot));
    }

    @Transactional
    public void updateReservation(UUID reservationId, UpdateReservationDto dto, AuthUserInfo userInfo) {
        TimeSlot timeSlot  = timeSlotDomainService.getOne(dto.timeSlotId());
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if(!canHandleReservation(reservation, userInfo)){
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        reservationDomainService.updateOne(reservation, dto, timeSlot);
    }

    @Transactional
    public void updateReservationStatus(UUID reservationId ,ReservationStatus status, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if(!canHandleReservation(reservation, userInfo)){
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        reservationDomainService.updateStatus(reservation, status);
    }

    @Transactional
    public void deleteReservation(UUID reservationId, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(reservationId);
        if (!canHandleReservation(reservation, userInfo)){
            throw new GlowGlowException(RESERVATION_NOT_OWNER);
        }
        reservationDomainService.deleteOne(reservation, userInfo.getEmail());
    }

    private boolean canHandleReservation(Reservation reservation, AuthUserInfo userInfo) {
        if (userInfo.getUserRole().equals(UserRole.CUSTOMER)) {
            return reservation.getCustomerId().equals(userInfo.getId());
        } else if (userInfo.getUserRole().equals(UserRole.PROVIDER)) {
            return reservation.getServiceProviderId().equals(userInfo.getId());
        }
        return false;
    }
}
