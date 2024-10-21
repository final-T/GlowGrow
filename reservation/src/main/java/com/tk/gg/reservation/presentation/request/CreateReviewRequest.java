package com.tk.gg.reservation.presentation.request;


import com.tk.gg.reservation.application.dto.CreateReviewDto;
import com.tk.gg.reservation.presentation.request.validation.RatingConstraint;
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

        @NotNull(message = "평점은 필수입니다.")
        @RatingConstraint
        Integer rating,

        @Size(min = 10, max = 500, message = "리뷰 내용은 최소 10자에서 최대 500자까지 가능합니다.")
        String content,
        @RatingConstraint
        Integer providerServiceQuality,  // 서비스 품질
        @RatingConstraint
        Integer providerProfessionalism, // 전문성
        @RatingConstraint
        Integer providerCommunication,  // 의사소통
        @RatingConstraint
        Integer providerPunctuality,    // 시간 준수
        @RatingConstraint
        Integer providerPriceSatisfaction,  // 가격 적정성
        @RatingConstraint
        Integer customerCommunication,  // 고객 의사소통
        @RatingConstraint
        Integer customerPunctuality,  // 고객 시간 준수
        @RatingConstraint
        Integer customerManners,  // 고객 매너
        @RatingConstraint
        Integer customerPaymentPromptness  // 결제 신속성

) {
    public CreateReviewDto toDto(){
        return CreateReviewDto.builder()
                .reservationId(reservationId)
                .reviewerId(reviewerId)
                .targetUserId(targetUserId)
                .rating(rating)
                .content(content)
                .providerServiceQuality(providerServiceQuality)
                .providerProfessionalism(providerProfessionalism)
                .providerCommunication(providerCommunication)
                .providerPunctuality(providerPunctuality)
                .providerPriceSatisfaction(providerPriceSatisfaction)
                .customerCommunication(customerCommunication)
                .customerPunctuality(customerPunctuality)
                .customerManners(customerManners)
                .customerPaymentPromptness(customerPaymentPromptness)
                .build();
    }
}
