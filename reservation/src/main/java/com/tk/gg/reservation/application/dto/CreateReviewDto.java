package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.Review;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateReviewDto(
        UUID reservationId,
        Long reviewerId,
        Long targetUserId,
        Integer rating,
        String content
) {
    public Review toEntity(Reservation reservation) {
        return Review.builder()
                .reservation(reservation)
                .reviewerId(reviewerId)
                .targetUserId(targetUserId)
                .rating(rating)
                .content(content)
                .build();
    }
}
