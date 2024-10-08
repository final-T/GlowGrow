package com.tk.gg.users.presenation.request;

public record UpdateProfileRequest(
        String profileImageUrl,
        String specialization,
        String bio
) {
}
