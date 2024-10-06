package com.tk.gg.reservation.domain.service;


import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;
import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.infrastructure.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@RequiredArgsConstructor
@Service
public class TimeSlotDomainService {

    private final TimeSlotRepository timeSlotRepository;

    public Page<TimeSlot> getAll(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return timeSlotRepository.searchTimeSlots(startDate, endDate, pageable);
    }

    // TODO : 커스텀 에러 메시지 Common 모듈에 추가하기
    public TimeSlot getOne(UUID id) {
        return timeSlotRepository.findByReservationId(id).orElseThrow(
                () -> new GlowGlowException(TIMESLOT_NO_EXIST)
        );
    }

    public TimeSlot create(TimeSlot timeSlot) {
        if (checkAlreadyExist(timeSlot)){
            throw new GlowGlowException(TIMESLOT_ALREADY_EXIST);
        }
        return timeSlotRepository.save(timeSlot);
    }

    public Boolean checkAlreadyExist(TimeSlot timeSlot) {
        return timeSlotRepository.findByAvailableDateEqualsAndAvailableTimeEquals(
                timeSlot.getAvailableDate(), timeSlot.getAvailableTime()
        ).isPresent();
    }

    public void updateOne(TimeSlot timeSlot, UpdateTimeSlotRequestDto dto) {
        timeSlot.update(dto);
    }

    public void deleteOne(TimeSlot timeSlot,String deletedBy) {
        timeSlot.delete(deletedBy);
    }
}
