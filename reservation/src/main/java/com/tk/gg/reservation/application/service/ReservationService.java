package com.tk.gg.reservation.application.service;


import com.tk.gg.reservation.application.dto.CreateReservationDto;
import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.TimeSlotDomainService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.presentation.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

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

    //TODO : 추가적인 검증 필요, 예약 <-> 예약관리 테이블 수정
    @Transactional
    public ReservationDto createReservation(CreateReservationDto dto) {
        TimeSlot timeSlot = timeSlotDomainService.getOne(dto.timeSlotId());
        return ReservationDto.from(reservationDomainService.create(dto, timeSlot));
    }

    //TODO : 예약 <-> 예약관리테이블 수정
    @Transactional
    public void updateReservation(UUID reservationId, UpdateReservationDto dto) {
        TimeSlot timeSlot  = timeSlotDomainService.getOne(dto.timeSlotId());
        reservationDomainService.updateOne(reservationId, dto, timeSlot);
    }

    @Transactional
    public void updateReservationStatus(UUID reservationId ,ReservationStatus status) {
        reservationDomainService.updateStatus(reservationId, status);
    }

    @Transactional
    public void deleteReservation(UUID reservationId, String deletedBy) {
        reservationDomainService.deleteOne(reservationId,deletedBy);
    }
}
