package com.tk.gg.users.presenation.request;

public record WorkExperienceRequest(
        String companyName,
        String position,
        Integer experience
) {
}
