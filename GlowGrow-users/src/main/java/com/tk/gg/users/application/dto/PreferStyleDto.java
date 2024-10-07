package com.tk.gg.users.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PreferStyleDto(
        UUID preferLocationId,
        ProfileDto profileDto,
        UUID styleId,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
}
