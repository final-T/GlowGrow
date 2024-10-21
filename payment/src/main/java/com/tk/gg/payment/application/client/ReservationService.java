package com.tk.gg.payment.application.client;

import com.tk.gg.common.kafka.payment.PaymentReservationResponseDto;

import java.util.UUID;

public interface ReservationService {
    PaymentReservationResponseDto getOneReservation(UUID reservationId, String authToken);
}
