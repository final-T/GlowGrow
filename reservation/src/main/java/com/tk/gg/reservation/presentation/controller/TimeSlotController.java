package com.tk.gg.reservation.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.TimeSlotService;
import com.tk.gg.reservation.presentation.request.CreateTimeSlotRequest;
import com.tk.gg.reservation.presentation.request.UpdateTimeSlotRequest;
import com.tk.gg.reservation.presentation.response.TimeSlotResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    public GlobalResponse<TimeSlotResponse> create(
            @RequestBody @Valid CreateTimeSlotRequest request,
            @AuthUser AuthUserInfo userInfo
            ) {
        return ApiUtils.success(
                TIMESLOT_CREATE_SUCCESS.getMessage(),
                TimeSlotResponse.from(timeSlotService.createTimeSlot(request.toDto(), userInfo))
        );
    }

    @GetMapping
    public GlobalResponse<Page<TimeSlotResponse>> getAll(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @PageableDefault(sort = {"availableDate"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiUtils.success(
                TIMESLOT_RETRIEVE_SUCCESS.getMessage(),
                timeSlotService.getAllTimeSlot(startDate, endDate, pageable).map(TimeSlotResponse::from)
        );
    }

    @GetMapping("/{timeSlotId}")
    public GlobalResponse<TimeSlotResponse> getOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId
    ) {
        return ApiUtils.success(
                TIMESLOT_RETRIEVE_SUCCESS.getMessage(),
                TimeSlotResponse.from(timeSlotService.getTimeSlotDetails(timeSlotId))
        );
    }

    @PutMapping("/{timeSlotId}")
    public GlobalResponse<String> updateOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId,
            @RequestBody @Valid UpdateTimeSlotRequest request,
            @AuthUser AuthUserInfo userInfo
    ) {
        timeSlotService.updateTimeSlot(timeSlotId, request.toDto(), userInfo);
        return ApiUtils.success(TIMESLOT_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{timeSlotId}")
    public GlobalResponse<String> deleteOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId,
            @AuthUser AuthUserInfo userInfo
    ) {
        timeSlotService.deleteTimeSlot(timeSlotId, userInfo);
        return ApiUtils.success(TIMESLOT_DELETE_SUCCESS.getMessage());
    }
}
