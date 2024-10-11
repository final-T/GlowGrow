package com.tk.gg.reservation.presentation.controller;


import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.ReservationService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.presentation.request.CreateReservationRequest;
import com.tk.gg.reservation.presentation.request.UpdateReservationRequest;
import com.tk.gg.reservation.presentation.request.UpdateReservationStatusRequest;
import com.tk.gg.reservation.presentation.response.ReservationResponse;
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
@RequestMapping("/api/reservations")
@Tag(name = "Reservation API", description = "예약 기능")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "생성 API", description = "예약(reservation) 정보를 생성합니다.**[ROLE: Customer,Master]**")
    public GlobalResponse<ReservationResponse> createReservation(
            @RequestBody @Valid CreateReservationRequest request
    ) {
        return ApiUtils.success(RESERVATION_CREATE_SUCCESS.getMessage(),
                ReservationResponse.from(reservationService.createReservation(request.toDto()))
        );
    }

    @GetMapping
    @Operation(summary = "전체 조회 API", description = "예약(reservation) 목록을 조회합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<Page<ReservationResponse>> getAllReservations(
            @Parameter(name = "startDate", description = "검색 시작 범위 날짜",example = "yyyy-MM-dd")
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(name = "endDate", description = "검색 끝 범위 날짜",example = "yyyy-MM-dd")
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @Parameter(name = "status", description = "예약 상태", example = "ACCEPT")
            @RequestParam(value = "status", required = false) ReservationStatus status,
            @ParameterObject @PageableDefault(sort = {"reservationDate"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ApiUtils.success(RESERVATION_RETRIEVE_SUCCESS.getMessage(),
                reservationService.searchReservations(startDate, endDate, status, pageable).map(ReservationResponse::from)
        );
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "단건 조회 API", description = "예약(reservation) 정보를 조회합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<ReservationResponse> getOneReservation(
            @PathVariable(value = "reservationId") UUID reservationId
    ) {
        return ApiUtils.success(RESERVATION_RETRIEVE_SUCCESS.getMessage(),
                ReservationResponse.from(reservationService.getOneReservation(reservationId))
        );
    }

    @PutMapping("/{reservationId}")
    @Operation(summary = "수정 API", description = "예약(reservation) 정보를 수정합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<String> updateReservation(
            @PathVariable(value = "reservationId") UUID reservationId,
            @RequestBody @Valid UpdateReservationRequest request,
            @AuthUser AuthUserInfo userInfo
    ) {
        reservationService.updateReservation(reservationId, request.toDto(), userInfo);
        return ApiUtils.success(RESERVATION_UPDATE_SUCCESS.getMessage());
    }

    @PatchMapping("/{reservationId}/status")
    @Operation(summary = "상태 수정 API", description = "예약(reservation) 상태를 수정합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<String> updateReservationStatus(
            @PathVariable(value = "reservationId") UUID reservationId,
            @RequestBody @Valid UpdateReservationStatusRequest reservationStatus,
            @AuthUser AuthUserInfo userInfo
    ) {
        reservationService.updateReservationStatus(reservationId, reservationStatus.reservationStatus(), userInfo);
        return ApiUtils.success(RESERVATION_UPDATE_STATUS_SUCCESS.getMessage());
    }

    @DeleteMapping("/{reservationId}")
    @Operation(summary = "삭제 API", description = "예약(reservation) 정보를 삭제합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<String> deleteReservation(
            @PathVariable(value = "reservationId") UUID reservationId,
            @AuthUser AuthUserInfo userInfo
    ) {
        reservationService.deleteReservation(reservationId, userInfo);
        return ApiUtils.success(RESERVATION_DELETE_SUCCESS.getMessage());
    }
}
