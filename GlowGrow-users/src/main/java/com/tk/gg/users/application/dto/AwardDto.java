package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.Award;
import com.tk.gg.users.domain.model.Profile;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
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

    public static AwardDto of(String awardName, String awardLevel, String awardDate, String organization) {
        return AwardDto.builder()
                .awardName(awardName)
                .awardLevel(awardLevel)
                .awardDate(awardDate)
                .organization(organization)
                .build();
    }

    public Award toEntity(Profile profile){
        return Award.create(profile, awardName, awardLevel, awardDate, organization);
    }

    public static AwardDto from(Award award) {
        return AwardDto.builder()
                .awardId(award.getAwardId())
                .awardName(award.getAwardName())
                .awardLevel(award.getAwardLevel())
                .awardDate(award.getAwardDate())
                .organization(award.getOrganization())
                .isDeleted(award.getIsDeleted())
                .createdAt(award.getCreatedAt())
                .createdBy(award.getCreatedBy())
                .updatedAt(award.getUpdatedAt())
                .updatedBy(award.getUpdatedBy())
                .deletedAt(award.getDeletedAt())
                .deletedBy(award.getDeletedBy())
                .build();
    }
}
