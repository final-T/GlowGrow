package com.tk.gg.reservation.domain.service;

import com.tk.gg.reservation.application.dto.CreateReservationDto;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.infrastructure.repository.ReservationRepository;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

//TODO : 예약 동시성 락 처리 고민
@RequiredArgsConstructor
@Service
public class ReservationDomainService {

    private final ReservationRepository reservationRepository;

    public Page<Reservation> searchReservations(LocalDate startDate, LocalDate endDate, ReservationStatus status,Pageable pageable) {
        return reservationRepository.searchReservations(startDate, endDate, status, pageable);
    }

    public Reservation getOne(UUID id) {
        return reservationRepository.findByReservationId(id).orElseThrow(
                () -> new IllegalArgumentException("ID 에 해당하는 reservation 정보가 없습니다.")
        );
    }

    public Reservation create(CreateReservationDto dto, TimeSlot timeSlot) {
        timeSlot.updateIsReserved(true);
        return reservationRepository.save(dto.toEntity(timeSlot));
    }

    //TODO : 예약 취소, 거절 가능 날짜 검증 추가
    public void updateOne(UUID id, UpdateReservationDto dto, TimeSlot timeSlot) {
        Reservation reservation = getOne(id);
        // 예약 시간을 바꿀 때 한번 더 빈 시간대인지 검증
        if(!reservation.getTimeSlot().equals(timeSlot) && timeSlot.getIsReserved().equals(true))
            throw new BadRequestException("[예약변경실패] 이미 예약되어 있는 시간입니다.");
        // 사용자 취소 시 예약 가능시간테이블 정보 상태 수정
        if (dto.reservationStatus().equals(ReservationStatus.CANCEL)){
            reservation.getTimeSlot().updateIsReserved(false);
        }
        reservation.update(dto, timeSlot);
    }

    //TODO : 예약 취소, 거절 가능 날짜 검증 추가
    public void updateStatus(UUID id ,ReservationStatus status){
        Reservation reservation = getOne(id);
        // 제공자 취소,거절 시 예약 가능시간테이블 정보 상태 수정
        if (status.equals(ReservationStatus.CANCEL) || status.equals(ReservationStatus.REFUSED)){
            reservation.getTimeSlot().updateIsReserved(false);
        }
        reservation.updateStatus(status);
    }

    public void deleteOne(UUID id,String deletedBy) {
        Reservation reservation = getOne(id);
        reservation.getTimeSlot().updateIsReserved(false);
        reservation.delete(deletedBy);
    }
}
