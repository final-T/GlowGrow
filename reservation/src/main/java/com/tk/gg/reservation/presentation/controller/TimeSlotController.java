package com.tk.gg.reservation.presentation.controller;

import com.tk.gg.reservation.application.service.TimeSlotService;
import com.tk.gg.reservation.presentation.request.CreateTimeSlotRequest;
import com.tk.gg.reservation.presentation.request.UpdateTimeSlotRequest;
import com.tk.gg.reservation.presentation.response.TimeSlotResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// TODO : Security 추가 후 권한 처리
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/time-slot")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    public TimeSlotResponse create(
            @RequestBody @Valid CreateTimeSlotRequest request
            ){
        return TimeSlotResponse.from(timeSlotService.createTimeSlot(request.toDto()));
    }

    //TODO : querydsl 페이징
    @GetMapping
    public List<TimeSlotResponse> getAll(
    ){
        return timeSlotService.getAllTimeSlot().stream().map(TimeSlotResponse::from).toList();
    }

    @GetMapping("/{timeSlotId}")
    public TimeSlotResponse getOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId
    ){
        return TimeSlotResponse.from(timeSlotService.getTimeSlotDetails(timeSlotId));
    }

    @PutMapping("/{timeSlotId}")
    public void updateOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId,
            @RequestBody @Valid UpdateTimeSlotRequest request
    ){
        timeSlotService.updateTimeSlot(timeSlotId, request.toDto());
    }

    @DeleteMapping("/{timeSlotId}")
    public void deleteOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId
    ){
        // TODO : 유저 헤더 추가
        timeSlotService.deleteTimeSlot(timeSlotId, "deletedBy");
    }
}
