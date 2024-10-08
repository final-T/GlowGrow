package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.PreferLocationDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record LocationResponse(
        UUID preferLocationId,
        String locationName
) {
    public static LocationResponse from(PreferLocationDto dto){
        return LocationResponse.builder()
                .preferLocationId(dto.preferLocationId())
                .locationName(dto.locationName())
                .build();
    }
}
