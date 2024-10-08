package com.tk.gg.reservation.infrastructure.messaging;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tk.gg.common.enums.UserRole;
import lombok.Builder;

import java.util.UUID;

@JsonSerialize
@JsonDeserialize
@Builder
public record GradeForReviewEventDto(
        Long reviewerId,
        Long targetUserId,
        UserRole userType,
        UUID reviewId
) {
}
