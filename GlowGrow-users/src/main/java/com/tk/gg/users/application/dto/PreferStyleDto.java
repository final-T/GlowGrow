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
        String styleName,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static PreferStyleDto of(String styleName) {
        return PreferStyleDto.builder()
                .styleName(styleName)
                .build();
    }

    public PreferStyle toEntity(Profile profile) {
        return PreferStyle.create(profile, styleName);
    }

    public static PreferStyleDto from(PreferStyle preferStyle) {
        return PreferStyleDto.builder()
                .preferStyleId(preferStyle.getPreferStyleId())
                .styleName(preferStyle.getStyleName())
                .isDeleted(preferStyle.getIsDeleted())
                .createdAt(preferStyle.getCreatedAt())
                .createdBy(preferStyle.getCreatedBy())
                .updatedAt(preferStyle.getUpdatedAt())
                .updatedBy(preferStyle.getUpdatedBy())
                .deletedAt(preferStyle.getDeletedAt())
                .deletedBy(preferStyle.getDeletedBy())
                .build();
    }
}
