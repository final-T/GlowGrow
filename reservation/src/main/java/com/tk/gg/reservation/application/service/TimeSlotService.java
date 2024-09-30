package com.tk.gg.reservation.application.service;

import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;
import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;
import com.tk.gg.reservation.domain.service.TimeSlotDomainService;
import com.tk.gg.reservation.application.dto.TimeSlotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TimeSlotService {

    private final TimeSlotDomainService timeSlotDomainService;

    @Transactional
    public TimeSlotDto createTimeSlot(CreateTimeSlotRequestDto dto) {
        return TimeSlotDto.from(timeSlotDomainService.create(dto));
    }

    @Transactional(readOnly = true)
    public List<TimeSlotDto> getAllTimeSlot() {
        return timeSlotDomainService.getAll().stream().map(TimeSlotDto::from).toList();
    }

    @Transactional(readOnly = true)
    public TimeSlotDto getTimeSlotDetails(UUID timeSlotId) {
        return TimeSlotDto.from(timeSlotDomainService.getOne(timeSlotId));
    }

    @Transactional
    public void updateTimeSlot(UUID timeSlotId ,UpdateTimeSlotRequestDto dto) {
        timeSlotDomainService.updateOne(timeSlotId, dto);
    }

    @Transactional
    public void deleteTimeSlot(UUID timeSlotId, String deletedBy){
        timeSlotDomainService.deleteOne(timeSlotId, deletedBy);
    }
}
