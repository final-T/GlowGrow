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
}
