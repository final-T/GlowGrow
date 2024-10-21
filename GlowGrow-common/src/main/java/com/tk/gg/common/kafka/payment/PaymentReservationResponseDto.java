package com.tk.gg.common.kafka.payment;

import java.util.UUID;

public record PaymentReservationResponseDto(
        UUID reservationId,
        Long customerId,
        Long serviceProviderId,
        Integer price
) {
}
