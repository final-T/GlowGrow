package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.Profile;
import com.tk.gg.users.domain.model.User;
import com.tk.gg.users.presenation.request.ProfileRequest;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ProfileDto(
        UUID profileId,
        UserDto userDto,
        String profileImageUrl,
        String specialization,
        String bio,
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
