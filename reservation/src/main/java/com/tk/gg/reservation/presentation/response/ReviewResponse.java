package com.tk.gg.reservation.presentation.response;

import com.tk.gg.reservation.application.dto.ReviewDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReviewResponse(
        UUID id,
        Long reviewerId,
        Long targetUserId,
        Integer rating,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ReviewResponse from(ReviewDto dto) {
        return ReviewResponse.builder()
                .id(dto.id())
                .reviewerId(dto.reviewerId())
                .targetUserId(dto.targetUserId())
                .rating(dto.rating())
                .content(dto.content())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }
}
