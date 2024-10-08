package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.Profile;
import com.tk.gg.users.domain.model.WorkExperience;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record WorkExperienceDto(
        UUID workExperienceId,
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
    public static WorkExperienceDto of(String companyName, String position, Integer experience) {
        return WorkExperienceDto.builder()
                .companyName(companyName)
                .position(position)
                .experience(experience)
                .build();
    }

    public WorkExperience toEntity(Profile profile) {
        return WorkExperience.create(profile, companyName, position, experience);
    }

    public static WorkExperienceDto from(WorkExperience workExperience) {
        return WorkExperienceDto.builder()
                .workExperienceId(workExperience.getWorkExperienceId())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .experience(workExperience.getExperience())
                .isDeleted(workExperience.getIsDeleted())
                .createdAt(workExperience.getCreatedAt())
                .createdBy(workExperience.getCreatedBy())
                .updatedAt(workExperience.getUpdatedAt())
                .updatedBy(workExperience.getUpdatedBy())
                .deletedAt(workExperience.getDeletedAt())
                .deletedBy(workExperience.getDeletedBy())
                .build();
    }
}
