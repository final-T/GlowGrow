package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.PreferStyleDto;

import java.util.UUID;

public record StyleRequest(
        UUID styleId
) {
    public PreferStyleDto toDto() {
        return PreferStyleDto.of(styleId);
    }
}
