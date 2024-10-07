package com.tk.gg.users.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkExperienceDto(
        UUID awardId,
        ProfileDto profileDto,
        String companyName,
        String position,
        Integer experience,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
}
