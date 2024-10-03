package com.tk.gg.reservation.application.dto;

import lombok.Builder;

@Builder
public record UpdateReviewDto(
        Integer rating,
        String content
) {
}
