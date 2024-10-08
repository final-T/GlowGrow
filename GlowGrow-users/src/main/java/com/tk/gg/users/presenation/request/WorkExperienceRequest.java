package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.WorkExperienceDto;

public record WorkExperienceRequest(
        String companyName,
        String position,
        Integer experience
) {
    public WorkExperienceDto toDto() {
        return WorkExperienceDto.of(companyName, position, experience);
    }
}
