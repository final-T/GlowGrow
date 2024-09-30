package com.tk.gg.reservation.domain.service;


import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;
import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TimeSlotDomainService {

    private final TimeSlotRepository timeSlotRepository;

    public List<TimeSlot> getAll() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot getOne(UUID id) {
        return timeSlotRepository.findByReservationId(id).orElseThrow(
                () -> new IllegalArgumentException("ID 에 해당하는 timeSlot 정보가 없습니다.")
        );
    }

    public TimeSlot create(CreateTimeSlotRequestDto dto) {
        return timeSlotRepository.save(dto.toEntity());
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
