package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.AwardDto;

public record AwardRequest(
        String awardName,
        String awardLevel,
        String awardDate,
        String organization
) {
    public AwardDto toDto() {
        return AwardDto.of(awardName, awardLevel, awardDate, organization);
    }
}
