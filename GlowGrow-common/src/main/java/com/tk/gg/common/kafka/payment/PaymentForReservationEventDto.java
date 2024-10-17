package com.tk.gg.common.kafka.payment;

import java.util.UUID;

public record PaymentForReservationEventDto(
        UUID reservationId
) {
}
