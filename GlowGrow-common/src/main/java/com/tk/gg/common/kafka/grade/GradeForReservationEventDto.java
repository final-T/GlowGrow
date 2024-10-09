package com.tk.gg.common.kafka.grade;


import lombok.Builder;

import java.util.UUID;

@Builder
public record GradeForReservationEventDto(
        Long customerId,
        Long providerId,
        UUID reservationId
) {
}
