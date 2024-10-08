package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.PreferPrice;
import com.tk.gg.users.domain.model.Profile;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PreferPriceDto(
        UUID preferPriceId,
        ProfileDto profileDto,
        Long price,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static PreferPriceDto of(Long price) {
        return PreferPriceDto.builder().price(price).build();
    }

    public PreferPrice toEntity(Profile profile) {
        return PreferPrice.create(profile, price);
    }

    public static PreferPriceDto from(PreferPrice preferPrice) {
        return PreferPriceDto.builder()
                .preferPriceId(preferPrice.getPreferPriceId())
                .price(preferPrice.getPrice())
                .isDeleted(preferPrice.getIsDeleted())
                .createdAt(preferPrice.getCreatedAt())
                .createdBy(preferPrice.getCreatedBy())
                .updatedAt(preferPrice.getUpdatedAt())
                .updatedBy(preferPrice.getUpdatedBy())
                .deletedAt(preferPrice.getDeletedAt())
                .deletedBy(preferPrice.getDeletedBy())
                .build();

    }
}
