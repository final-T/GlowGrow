package com.tk.gg.payment.infrastructure.client;

import com.tk.gg.common.kafka.payment.PaymentReservationResponseDto;
import com.tk.gg.common.response.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "reservation-service")
public interface ReservationFeignClient {

    @GetMapping("/api/reservations/{reservationId}")
    GlobalResponse<PaymentReservationResponseDto> getOneReservation(
            @PathVariable("reservationId") UUID reservationId,
            @RequestHeader("Authorization") String authorizationHeader
    );
}
