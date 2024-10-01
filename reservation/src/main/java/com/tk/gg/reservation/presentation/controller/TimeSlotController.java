package com.tk.gg.reservation.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.TimeSlotService;
import com.tk.gg.reservation.presentation.request.CreateTimeSlotRequest;
import com.tk.gg.reservation.presentation.request.UpdateTimeSlotRequest;
import com.tk.gg.reservation.presentation.response.TimeSlotResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

// TODO : Security 추가 후 권한 처리, 유저 정보 검증(FeignClient)
// TODO : 응답 메시지 Enum common 에 추가하기
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    public GlobalResponse<TimeSlotResponse> create(
            @RequestBody @Valid CreateTimeSlotRequest request
    ) {
        return ApiUtils.success(
                "예약 가능 테이블 데이터 생성 성공",
                TimeSlotResponse.from(timeSlotService.createTimeSlot(request.toDto()))
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
                "예약 가능 테이블 정보 조회 성공",
                timeSlotService.getAllTimeSlot(startDate, endDate, pageable).map(TimeSlotResponse::from)
        );
    }

    @GetMapping("/{timeSlotId}")
    public GlobalResponse<TimeSlotResponse> getOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId
    ) {
        return ApiUtils.success(
                "예약 가능 테이블 정보 조회 성공",
                TimeSlotResponse.from(timeSlotService.getTimeSlotDetails(timeSlotId))
        );
    }

    @PutMapping("/{timeSlotId}")
    public GlobalResponse<String> updateOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId,
            @RequestBody @Valid UpdateTimeSlotRequest request
    ) {
        timeSlotService.updateTimeSlot(timeSlotId, request.toDto());
        return ApiUtils.success("예약 가능 테이블 정보 수정 성공");
    }

    @DeleteMapping("/{timeSlotId}")
    public GlobalResponse<String> deleteOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId
    ) {
        // TODO : 유저 헤더 추가
        timeSlotService.deleteTimeSlot(timeSlotId, "deletedBy");
        return ApiUtils.success("예약 가능 테이블 정보 삭제 성공");
    }
}
