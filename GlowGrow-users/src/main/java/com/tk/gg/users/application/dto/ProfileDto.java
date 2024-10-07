package com.tk.gg.users.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileDto(
        UUID profileId,
        UserDto userDto,
        String profileImageUrl,
        String specialization,
        String bio,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
}
