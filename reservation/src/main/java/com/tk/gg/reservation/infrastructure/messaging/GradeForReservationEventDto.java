package com.tk.gg.reservation.infrastructure.messaging;


import lombok.Builder;

import java.util.UUID;

@Builder
public record GradeForReservationEventDto(
        Long customerId,
        Long providerId,
        UUID reservationId
) {
}
