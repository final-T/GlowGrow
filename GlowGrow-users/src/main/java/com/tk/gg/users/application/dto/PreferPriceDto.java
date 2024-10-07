package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.PreferPrice;
import com.tk.gg.users.domain.model.Profile;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PreferPriceDto(
        UUID preferLocationId,
        ProfileDto profileDto,
        UUID priceId,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static PreferPriceDto of(UUID priceId) {
        return PreferPriceDto.builder().priceId(priceId).build();
    }

    public PreferPrice toEntity(Profile profile) {
        return PreferPrice.create(profile, priceId);
    }
}
