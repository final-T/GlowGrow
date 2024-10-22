package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.ProfileDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ProfilePageResponse(
        UUID profileId,
        Long userId,
        String userName,
        String profileImageUrl,
        List<StyleResponse> styleList,
        List<PriceResponse> priceList,
        List<LocationResponse> locationList,
        LocalDateTime createdAt
) {

    public static ProfilePageResponse from(ProfileDto dto){
        return ProfilePageResponse.builder()
                .profileId(dto.profileId())
                .userId(dto.userDto().userId())
                .userName(dto.userDto().username())
                .profileImageUrl(dto.profileImageUrl())
                .styleList(dto.preferStylesDto().stream().map(StyleResponse::from).toList())
                .priceList(dto.preferPricesDto().stream().map(PriceResponse::from).toList())
                .locationList(dto.preferLocationsDto().stream().map(LocationResponse::from).toList())
                .createdAt(dto.createdAt())
                .build();
    }
}
