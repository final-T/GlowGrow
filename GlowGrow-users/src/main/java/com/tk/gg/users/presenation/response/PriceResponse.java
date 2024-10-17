package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.PreferPriceDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PriceResponse(
        UUID preferPriceId,
        Long price
) {
    public static PriceResponse from(PreferPriceDto dto){
        return PriceResponse.builder()
                .preferPriceId(dto.preferPriceId())
                .price(dto.price())
                .build();
    }
}
