package com.tk.gg.reservation.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.client.UserService;
import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;
import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.service.TimeSlotDomainService;
import com.tk.gg.reservation.application.dto.TimeSlotDto;
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
public class TimeSlotService {

    private final UserService userService;
    private final TimeSlotDomainService timeSlotDomainService;

    @Transactional
    public TimeSlotDto createTimeSlot(CreateTimeSlotRequestDto dto, AuthUserInfo userInfo) {
        if (!userService.isUserExistsByEmail(userInfo.getEmail()) ||
                !userInfo.getId().equals(dto.serviceProviderId())) {
            throw new GlowGlowException(AUTH_INVALID_CREDENTIALS);
        }
        return TimeSlotDto.from(timeSlotDomainService.create(dto.toEntity()));
    }

    @Transactional(readOnly = true)
    public Page<TimeSlotDto> getAllTimeSlot(LocalDate startDate, LocalDate endDate, Pageable pageable) {

        return timeSlotDomainService.getAll(startDate, endDate, pageable).map(TimeSlotDto::from);
    }

    @Transactional(readOnly = true)
    public TimeSlotDto getTimeSlotDetails(UUID timeSlotId) {
        return TimeSlotDto.from(timeSlotDomainService.getOne(timeSlotId));
    }

    @Transactional
    public void updateTimeSlot(UUID timeSlotId, UpdateTimeSlotRequestDto dto, AuthUserInfo userInfo) {
        TimeSlot timeSlot = timeSlotDomainService.getOne(timeSlotId);
        checkTimeSlotOwner(userInfo, timeSlot);
        timeSlotDomainService.updateOne(timeSlot, dto);
    }

    @Transactional
    public void deleteTimeSlot(UUID timeSlotId, AuthUserInfo userInfo) {
        TimeSlot timeSlot = timeSlotDomainService.getOne(timeSlotId);
        checkTimeSlotOwner(userInfo, timeSlot);
        timeSlotDomainService.deleteOne(timeSlot, userInfo.getEmail());
    }

    private void checkTimeSlotOwner(AuthUserInfo userInfo, TimeSlot timeSlot) {
        if(!userInfo.getUserRole().equals(UserRole.MASTER)){
            if(!timeSlot.getServiceProviderId().equals(userInfo.getId()))
                throw new GlowGlowException(TIMESLOT_NOT_OWNER);    
        }
    }
}
