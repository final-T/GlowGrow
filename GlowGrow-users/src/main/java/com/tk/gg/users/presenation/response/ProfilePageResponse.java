package com.tk.gg.users.presenation.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProfilePageResponse(
        UUID profileId,
        Long userId,
        String userName,
        String profileImageUrl,
        List<StyleResponse> styleList,
        List<PriceResponse> priceList,
        List<LocationResponse> locationList,
        LocalDateTime createdAt
) {
}
