package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ProfileResponse(
        UUID profileId,
        Long userId,
        String userName,
        String profileImageUrl,
        String specialization,
        String bio,
        List<StyleResponse> styles,
        List<LocationResponse> locations,
        List<PriceResponse> prices,
        List<AwardResponse> awards,
        List<WorkExperienceResponse> workExperiences,
        LocalDateTime createdAt

) {
    public static ProfileResponse from(ProfileDto dto) {

        return ProfileResponse.builder()
                .profileId(dto.profileId())
                .userId(dto.userDto().userId())
                .userName(dto.userDto().username())
                .profileImageUrl(dto.profileImageUrl())
                .specialization(dto.specialization())
                .bio(dto.bio())
                .styles(dto.preferStylesDto().stream().map(StyleResponse::from).toList())
                .locations(dto.preferLocationsDto().stream().map(LocationResponse::from).toList())
                .prices(dto.preferPricesDto().stream().map(PriceResponse::from).toList())
                .awards(dto.awardsDto().stream().map(AwardResponse::from).toList())
                .workExperiences(dto.workExperiencesDto().stream().map(WorkExperienceResponse::from).toList())
                .build();
    }
}
