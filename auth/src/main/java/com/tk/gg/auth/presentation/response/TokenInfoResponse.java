package com.tk.gg.auth.presentation.response;

import lombok.Builder;

@Builder
public record TokenInfoResponse(
        Long userId,
        String email,
        String accessToken,
        String refreshToken
) {
}
