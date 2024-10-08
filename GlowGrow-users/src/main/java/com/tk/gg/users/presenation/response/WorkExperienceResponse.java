package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.WorkExperienceDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkExperienceResponse(
        UUID workExperienceId,
        String companyName,
        String position,
        Integer experience
) {
    public static WorkExperienceResponse from(WorkExperienceDto dto) {
        return WorkExperienceResponse.builder()
                .workExperienceId(dto.workExperienceId())
                .companyName(dto.companyName())
                .position(dto.position())
                .experience(dto.experience())
                .build();
    }
}
