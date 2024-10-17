package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.PreferPriceDto;

import java.util.UUID;

public record PriceRequest(
        Long price
) {
    public PreferPriceDto toDto() {
        return PreferPriceDto.of(price);
    }
}
