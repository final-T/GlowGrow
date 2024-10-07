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
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public GlobalResponse<ReservationResponse> createReservation(
            @RequestBody @Valid CreateReservationRequest request
    ) {
        return ApiUtils.success(RESERVATION_CREATE_SUCCESS.getMessage(),
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
    ) {
        return ApiUtils.success(RESERVATION_RETRIEVE_SUCCESS.getMessage(),
                reservationService.searchReservations(startDate, endDate, status, pageable).map(ReservationResponse::from)
        );
    }

    @GetMapping("/{reservationId}")
    public GlobalResponse<ReservationResponse> getOneReservation(
            @PathVariable(value = "reservationId") UUID reservationId
    ) {
        return ApiUtils.success(RESERVATION_RETRIEVE_SUCCESS.getMessage(),
                ReservationResponse.from(reservationService.getOneReservation(reservationId))
        );
    }

    @PutMapping("/{reservationId}")
    public GlobalResponse<String> updateReservation(
            @PathVariable(value = "reservationId") UUID reservationId,
            @RequestBody @Valid UpdateReservationRequest request,
            @AuthUser AuthUserInfo userInfo
    ) {
        reservationService.updateReservation(reservationId, request.toDto(), userInfo);
        return ApiUtils.success(RESERVATION_UPDATE_SUCCESS.getMessage());
    }

    @PatchMapping("/{reservationId}/status")
    public GlobalResponse<String> updateReservationStatus(
            @PathVariable(value = "reservationId") UUID reservationId,
            @RequestBody @Valid UpdateReservationStatusRequest reservationStatus,
            @AuthUser AuthUserInfo userInfo
    ) {
        reservationService.updateReservationStatus(reservationId, reservationStatus.getReservationStatus(), userInfo);
        return ApiUtils.success(RESERVATION_UPDATE_STATUS_SUCCESS.getMessage());
    }

    @DeleteMapping("/{reservationId}")
    public GlobalResponse<String> deleteReservation(
            @PathVariable(value = "reservationId") UUID reservationId,
            @AuthUser AuthUserInfo userInfo
    ) {
        reservationService.deleteReservation(reservationId, userInfo);
        return ApiUtils.success(RESERVATION_DELETE_SUCCESS.getMessage());
    }
}
