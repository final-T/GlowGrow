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
        UUID locationId,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static PreferLocationDto of(UUID locationId){
        return PreferLocationDto.builder()
                .locationId(locationId)
                .build();
    }

    public PreferLocation toEntity(Profile profile) {
        return PreferLocation.create(profile, locationId);
    }
}
