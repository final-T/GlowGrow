package com.tk.gg.reservation.domain.service;


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
                () -> new IllegalArgumentException("ID 에 해당하는 timeSlot 정보가 없습니다.")
        );
    }

    public TimeSlot create(CreateTimeSlotRequestDto dto) {
        if (checkAlreadyExist(dto)){
            throw new IllegalArgumentException("이미 존재하는 timeSlot 정보입니다.");
        }
        return timeSlotRepository.save(dto.toEntity());
    }

    public Boolean checkAlreadyExist(CreateTimeSlotRequestDto dto) {
        return timeSlotRepository.findByAvailableDateEqualsAndAvailableTimeEquals(
                dto.availableDate(), dto.availableTime()
        ).isPresent();
    }

    public void updateOne(UUID id, UpdateTimeSlotRequestDto dto) {
        TimeSlot timeSlot = getOne(id);
        timeSlot.update(dto);
    }

    public void deleteOne(UUID id,String deletedBy) {
        TimeSlot timeSlot = getOne(id);
        timeSlot.delete(deletedBy);
    }
}
