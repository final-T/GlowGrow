package com.tk.gg.reservation.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.TimeSlotService;
import com.tk.gg.reservation.presentation.request.CreateTimeSlotRequest;
import com.tk.gg.reservation.presentation.request.UpdateTimeSlotRequest;
import com.tk.gg.reservation.presentation.response.TimeSlotResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "TimeSlot API", description = "예약타임슬롯(가능시간테이블) CRUD")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    @Operation(summary = "생성 API", description = "예약 가능 시간 (time-slot) 정보를 생성합니다.**[ROLE: Provider, Master]**")
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
    @Operation(summary = "전체 조회(페이징) API", description = "예약 가능 시간 (time-slot)의 모든 정보를 조회합니다. 날짜 범위로 검색이 가능합니다.**[ROLE: AuthenticatedUser]**")
    public GlobalResponse<Page<TimeSlotResponse>> getAll(
            @Parameter(name = "startDate", description = "검색 시작 범위 날짜(yyyy-MM-dd)")
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(name = "endDate", description = "검색 끝 범위 날짜(yyyy-MM-dd)")
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @ParameterObject @PageableDefault(sort = {"availableDate"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiUtils.success(
                TIMESLOT_RETRIEVE_SUCCESS.getMessage(),
                timeSlotService.getAllTimeSlot(startDate, endDate, pageable).map(TimeSlotResponse::from)
        );
    }

    @GetMapping("/{timeSlotId}")
    @Operation(summary = "단건 조회 API", description = "ID에 맞는 예약 가능 시간 (time-slot) 정보를 조회합니다.**[ROLE: AuthenticatedUser]**")
    public GlobalResponse<TimeSlotResponse> getOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId
    ) {
        return ApiUtils.success(
                TIMESLOT_RETRIEVE_SUCCESS.getMessage(),
                TimeSlotResponse.from(timeSlotService.getTimeSlotDetails(timeSlotId))
        );
    }

    @PutMapping("/{timeSlotId}")
    @Operation(summary = "수정 API", description = "ID 에 맞는 예약 가능 시간 (time-slot) 정보를 수정합니다.**[ROLE: Provider, Master]**")
    public GlobalResponse<String> updateOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId,
            @RequestBody @Valid UpdateTimeSlotRequest request,
            @AuthUser AuthUserInfo userInfo
    ) {
        timeSlotService.updateTimeSlot(timeSlotId, request.toDto(), userInfo);
        return ApiUtils.success(TIMESLOT_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{timeSlotId}")
    @Operation(summary = "삭제 API", description = "ID 에 맞는 예약 가능 시간 (time-slot) 정보를 삭제합니다.**[ROLE: Provider, Master]**")
    public GlobalResponse<String> deleteOne(
            @PathVariable(value = "timeSlotId") UUID timeSlotId,
            @AuthUser AuthUserInfo userInfo
    ) {
        timeSlotService.deleteTimeSlot(timeSlotId, userInfo);
        return ApiUtils.success(TIMESLOT_DELETE_SUCCESS.getMessage());
    }
}
