package com.tk.gg.reservation.application.dto;

import com.tk.gg.reservation.domain.model.Review;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReviewWithReservationDto(
        UUID id,
        ReservationDto reservationDto,
        Long reviewerId,
        Long targetUserId,
        Integer rating,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        String deletedBy,
        LocalDateTime deletedAt
) {

    public static ReviewWithReservationDto from(Review review) {
        return ReviewWithReservationDto.builder()
                .id(review.getId())
                .reservationDto(ReservationDto.from(review.getReservation()))
                .reviewerId(review.getReviewerId())
                .targetUserId(review.getTargetUserId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdBy(review.getCreatedBy())
                .createdAt(review.getCreatedAt())
                .updatedBy(review.getUpdatedBy())
                .updatedAt(review.getUpdatedAt())
                .deletedBy(review.getDeletedBy())
                .deletedAt(review.getDeletedAt())
                .build();
    }
}
