package com.tk.gg.common.kafka.grade;


import com.tk.gg.common.enums.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record GradeForReviewEventDto(
        Long reviewerId,
        Long targetUserId,
        UserRole userType,
        UUID reviewId
) {
}
