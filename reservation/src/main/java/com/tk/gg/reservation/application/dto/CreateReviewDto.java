package com.tk.gg.reservation.application.dto;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.presentation.request.validation.RatingConstraint;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateReviewDto(
        UUID reservationId,
        Long reviewerId,
        Long targetUserId,
        Integer rating,
        String content,
        Integer providerServiceQuality,  // 서비스 품질
        Integer providerProfessionalism, // 전문성
        Integer providerCommunication,  // 의사소통
        Integer providerPunctuality,    // 시간 준수
        Integer providerPriceSatisfaction,  // 가격 적정성
        Integer customerCommunication,  // 고객 의사소통
        Integer customerPunctuality,  // 고객 시간 준수
        Integer customerManners,  // 고객 매너
        Integer customerPaymentPromptness  // 결제 신속성
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

    public GradeDto toGradeDto(UserRole userType, UUID reviewId) {
        if (userType.equals(UserRole.CUSTOMER)){
            return GradeDto.builder()
                    .userId(targetUserId)  // 리뷰 대상자의 Grade
                    .userType(userType)
                    .reviewId(reviewId)
                    .providerServiceQuality(providerServiceQuality)
                    .providerProfessionalism(providerProfessionalism)
                    .providerCommunication(providerCommunication)
                    .providerPunctuality(providerPunctuality)
                    .providerPriceSatisfaction(providerPriceSatisfaction)
                    .build();
        }else if (userType.equals(UserRole.PROVIDER)){
            return GradeDto.builder()
                    .userId(targetUserId)  // 리뷰 대상자의 Grade
                    .userType(userType)
                    .reviewId(reviewId)
                    .customerCommunication(customerCommunication)
                    .customerPunctuality(customerPunctuality)
                    .customerManners(customerManners)
                    .customerPaymentPromptness(customerPaymentPromptness)
                    .build();
        }
        //TODO : 유저 타입이 이상할 경우에 에러 Throw
        return null;
    }
}
