package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.AwardDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AwardResponse(
        UUID awardId,
        String awardName,
        String awardLevel,
        String awardDate,
        String organization
) {
    public static AwardResponse from(AwardDto dto){
        return AwardResponse.builder()
                .awardId(dto.awardId())
                .awardName(dto.awardName())
                .awardLevel(dto.awardLevel())
                .awardDate(dto.awardDate())
                .organization(dto.organization())
                .build();
    }
}
