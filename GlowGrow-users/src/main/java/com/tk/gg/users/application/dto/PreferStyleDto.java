package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.PreferStyle;
import com.tk.gg.users.domain.model.Profile;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PreferStyleDto(
        UUID preferStyleId,
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
    public static PreferStyleDto of(UUID styleId) {
        return PreferStyleDto.builder()
                .styleId(styleId)
                .build();
    }

    public PreferStyle toEntity(Profile profile) {
        return PreferStyle.create(profile, styleId);
    }
}
