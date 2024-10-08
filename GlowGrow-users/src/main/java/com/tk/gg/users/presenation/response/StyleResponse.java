package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.PreferStyleDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record StyleResponse(
        UUID preferStyleId,
        String styleName
) {
    public static StyleResponse from(PreferStyleDto dto){
        return StyleResponse.builder()
                .preferStyleId(dto.preferStyleId())
                .styleName(dto.styleName())
                .build();
    }
}
