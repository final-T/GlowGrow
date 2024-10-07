package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.PreferLocationDto;

import java.util.UUID;

public record LocationRequest(
        UUID locationId
) {
    public PreferLocationDto toDto() {
        return PreferLocationDto.of(locationId);
    }
}
