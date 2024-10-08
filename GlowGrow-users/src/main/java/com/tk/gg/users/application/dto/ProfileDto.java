package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.*;
import com.tk.gg.users.presenation.request.ProfileRequest;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ProfileDto(
        UUID profileId,
        UserDto userDto,
        String profileImageUrl,
        String specialization,
        String bio,
        List<PreferStyleDto> preferStylesDto,
        List<PreferLocationDto> preferLocationsDto,
        List<PreferPriceDto> preferPricesDto,
        List<AwardDto> awardsDto,
        List<WorkExperienceDto> workExperiencesDto,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {

    public static ProfileDto of(UserDto userDto, String profileImageUrl, String specialization, String bio) {
        return ProfileDto.builder()
                .userDto(userDto)
                .profileImageUrl(profileImageUrl)
                .specialization(specialization)
                .bio(bio)
                .build();
    }

    public static ProfileDto of(UserDto userDto, ProfileRequest profileRequest) {
        return ProfileDto.of(userDto, profileRequest.profileImageUrl(), profileRequest.specialization(), profileRequest.bio());
    }

    public static ProfileDto from(Profile profile) {
        return ProfileDto.builder()
                .profileId(profile.getProfileId())
                .userDto(UserDto.from(profile.getUser()))
                .profileImageUrl(profile.getProfileImageUrl())
                .specialization(profile.getSpecialization())
                .bio(profile.getBio())

                .preferLocationsDto(profile.getPreferLocations() != null ?
                        profile.getPreferLocations().stream().map(PreferLocationDto::from).toList() : null)

                .preferStylesDto(profile.getPreferStyles() != null ?
                        profile.getPreferStyles().stream().map(PreferStyleDto::from).toList() : null)

                .preferPricesDto(profile.getPreferPrices() != null ?
                        profile.getPreferPrices().stream().map(PreferPriceDto::from).toList() : null)

                .awardsDto(profile.getAwards() != null ? profile.getAwards().stream().map(AwardDto::from).toList() : null)

                .workExperiencesDto(profile.getWorkExperiences() != null ?
                        profile.getWorkExperiences().stream().map(WorkExperienceDto::from).toList() : null)

                .isDeleted(profile.getIsDeleted())
                .createdAt(profile.getCreatedAt())
                .createdBy(profile.getCreatedBy())
                .updatedAt(profile.getUpdatedAt())
                .updatedBy(profile.getUpdatedBy())
                .deletedAt(profile.getDeletedAt())
                .deletedBy(profile.getDeletedBy())
                .build();
    }

    public Profile toEntity() {
        return Profile.create(User.of(userDto), profileImageUrl, specialization, bio);
    }
}
