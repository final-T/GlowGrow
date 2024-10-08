package com.tk.gg.reservation.infrastructure.messaging;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.util.UUID;

@JsonSerialize
@JsonDeserialize
@Builder
public record GradeForReservationEventDto(
        Long customerId,
        Long providerId,
        UUID reservationId
) {
}
