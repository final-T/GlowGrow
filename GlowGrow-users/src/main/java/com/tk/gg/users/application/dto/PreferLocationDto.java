package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.PreferLocation;
import com.tk.gg.users.domain.model.Profile;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PreferLocationDto(
        UUID preferLocationId,
        ProfileDto profileDto,
        String locationName,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static PreferLocationDto of(String locationName){
        return PreferLocationDto.builder()
                .locationName(locationName)
                .build();
    }

    public PreferLocation toEntity(Profile profile) {
        return PreferLocation.create(profile, locationName);
    }

    public static PreferLocationDto from(PreferLocation preferLocation) {
        return PreferLocationDto.builder()
                .preferLocationId(preferLocation.getPreferLocationId())
                .locationName(preferLocation.getLocationName())
                .isDeleted(preferLocation.getIsDeleted())
                .createdAt(preferLocation.getCreatedAt())
                .createdBy(preferLocation.getCreatedBy())
                .updatedAt(preferLocation.getUpdatedAt())
                .updatedBy(preferLocation.getUpdatedBy())
                .deletedAt(preferLocation.getDeletedAt())
                .deletedBy(preferLocation.getDeletedBy())
                .build();
    }
}
