package com.tk.gg.common.kafka.grade;

import lombok.Builder;
import java.util.UUID;

@Builder
public record GradeForReportEventDto(
        Long targetUserId,
        UUID reservationId
) {
}
