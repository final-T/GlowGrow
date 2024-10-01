package com.tk.gg.reservation.presentation.controller;


import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.ReservationService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.presentation.request.CreateReservationRequest;
import com.tk.gg.reservation.presentation.request.UpdateReservationRequest;
import com.tk.gg.reservation.presentation.response.ReservationResponse;
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

//TODO : Security, 유저  검증
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public GlobalResponse<ReservationResponse> createReservation(
            @RequestBody @Valid CreateReservationRequest request
    ){
        return ApiUtils.success("예약 생성 성공",
                ReservationResponse.from(reservationService.createReservation(request.toDto()))
        );
    }

    @GetMapping
    public GlobalResponse<Page<ReservationResponse>> getAllReservations(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "status", required = false) ReservationStatus status,
            @PageableDefault(sort = {"reservationDate"}, direction = Sort.Direction.ASC) Pageable pageable
    ){
        return ApiUtils.success("예약 전체 조회 성공",
                reservationService.searchReservations(startDate, endDate, status, pageable).map(ReservationResponse::from)
        );
    }

    @GetMapping("/{reservationId}")
    public GlobalResponse<ReservationResponse> getOneReservation(
            @PathVariable(value = "reservationId") UUID reservationId
    ){
        return ApiUtils.success("예약 단건 조회 성공",
                ReservationResponse.from(reservationService.getOneReservation(reservationId))
        );
    }

    @PutMapping("/{reservationId}")
    public GlobalResponse<String> updateReservation(
            @PathVariable(value = "reservationId") UUID reservationId,
            @RequestBody @Valid UpdateReservationRequest request
    ){
        reservationService.updateReservation(reservationId, request.toDto());
        return ApiUtils.success("예약 수정 성공");
    }

    @PatchMapping("/{reservationId}/status")
    public GlobalResponse<String> updateReservationStatus(
            @PathVariable(value = "reservationId") UUID reservationId,
            @RequestBody ReservationStatus reservationStatus
    ){
        reservationService.updateReservationStatus(reservationId,reservationStatus);
        return ApiUtils.success("예약 상태 수정 성공");
    }

    @DeleteMapping("/{reservationId}")
    public GlobalResponse<String> deleteReservation(
            @PathVariable(value = "reservationId") UUID reservationId
    ){
        //TODO : deletedBy 유저 수정
        reservationService.deleteReservation(reservationId, "deletedBy");
        return ApiUtils.success("예약 삭제 성공");
    }
}
