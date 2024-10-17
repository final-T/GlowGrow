package com.tk.gg.reservation.presentation.request;

import com.tk.gg.reservation.application.dto.UpdateReviewDto;
import jakarta.validation.constraints.*;

public record UpdateReviewRequest(
        @Min(value = 1, message = "평점은 1에서 5까지 입니다.")
        @Max(value = 5, message = "평점은 1에서 5까지 입니다.")
        @NotNull(message = "평점은 필수입니다.")
        Integer rating,

        @Size(min = 10, max = 500, message = "리뷰 내용은 최소 10자에서 최대 500자까지 가능합니다.")
        @NotBlank(message = "리뷰 수정은 리뷰 내용이 필수입니다.")
        String content
) {

    public UpdateReviewDto toDto(){
        return UpdateReviewDto.builder()
                .rating(rating)
                .content(content)
                .build();
    }
}
