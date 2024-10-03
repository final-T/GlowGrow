package com.tk.gg.reservation.presentation.response;

import com.tk.gg.reservation.application.dto.ReviewDto;
import com.tk.gg.reservation.application.dto.ReviewWithReservationDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReviewWithReservationResponse(
        UUID id,
        ReservationResponse reservation,
        Long reviewerId,
        Long targetUserId,
        Integer rating,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ReviewWithReservationResponse from(ReviewWithReservationDto dto) {
        return ReviewWithReservationResponse.builder()
                .id(dto.id())
                .reservation(ReservationResponse.from(dto.reservationDto()))
                .reviewerId(dto.reviewerId())
                .targetUserId(dto.targetUserId())
                .rating(dto.rating())
                .content(dto.content())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }
}
