package com.tk.gg.users.presenation.request;

public record ProfileRequest(
        String profileImageUrl,
        String specialization,
        String bio
) {
}
