package com.tk.gg.users.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AwardDto(
        UUID awardId,
        ProfileDto profileDto,
        String awardName,
        String awardLevel,
        String awardDate,
        String organization,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
}
