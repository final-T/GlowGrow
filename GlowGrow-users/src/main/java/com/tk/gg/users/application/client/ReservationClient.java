package com.tk.gg.users.application.client;

import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.users.presenation.response.GradeResponse;
import com.tk.gg.users.presenation.response.ResultGradeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "reservation-service")
public interface ReservationClient {

    @GetMapping("api/grades")
    GlobalResponse<ResultGradeDto> getTotalGradeForUser();

    @GetMapping("/api/grades/users/{userId}/reservations/{reservationId}")
    GlobalResponse<GradeResponse> getGradeForUserAndReservation(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "reservationId") UUID reservationId
    );

    @GetMapping("/api/grades/users/{userId}/reviews/{reviewId}")
    GlobalResponse<GradeResponse> getGradeForUserAndReview(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "reviewId") UUID reviewId
    );
}
