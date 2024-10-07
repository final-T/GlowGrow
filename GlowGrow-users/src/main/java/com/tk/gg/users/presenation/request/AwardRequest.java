package com.tk.gg.users.presenation.request;

public record AwardRequest(
        String awardName,
        String awardLevel,
        String awardDate,
        String organization
) {
}
