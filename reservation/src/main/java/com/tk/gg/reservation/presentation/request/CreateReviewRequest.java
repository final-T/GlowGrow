package com.tk.gg.reservation.presentation.request;


import com.tk.gg.reservation.application.dto.CreateReviewDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateReviewRequest(
        @NotNull(message = "관련 예약 ID 는 필수입니다.")
        UUID reservationId,

        @NotNull(message = "리뷰 작성자 ID 는 필수입니다.")
        Long reviewerId,

        @NotNull(message = "리뷰 대상자 ID 는 필수입니다.")
        Long targetUserId,

        @Min(value = 1, message = "평점은 1에서 5까지 입니다.")
        @Max(value = 5, message = "평점은 1에서 5까지 입니다.")
        @NotNull(message = "평점은 필수입니다.")
        Integer rating,

        @Size(min = 10, max = 500, message = "리뷰 내용은 최소 10자에서 최대 500자까지 가능합니다.")
        String content
) {
    public CreateReviewDto toDto(){
        return CreateReviewDto.builder()
                .reservationId(reservationId)
                .reviewerId(reviewerId)
                .targetUserId(targetUserId)
                .rating(rating)
                .content(content)
                .build();
    }
}
