package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.PreferPriceDto;

import java.util.UUID;

public record PriceRequest(
        UUID priceId
) {
    public PreferPriceDto toDto() {
        return PreferPriceDto.of(priceId);
    }
}
