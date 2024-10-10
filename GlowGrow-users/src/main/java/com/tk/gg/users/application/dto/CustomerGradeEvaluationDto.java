package com.tk.gg.users.application.dto;

import com.tk.gg.common.enums.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CustomerGradeEvaluationDto(
        UUID id,
        Long userId,
        UserRole userType, // PROVIDER 또는 CUSTOMER,
        UUID reviewId,
        UUID reservationId,
        Integer customerCommunication,  // 고객 의사소통
        Integer customerPunctuality,  // 고객 시간 준수
        Integer customerManners,  // 고객 매너
        Integer customerPaymentPromptness  // 결제 신속성
) {
    public static CustomerGradeEvaluationDto of(
            UUID id, Long userId, UserRole userType, UUID reviewId, UUID reservationId,
            Integer customerCommunication, Integer customerPunctuality, Integer customerManners, Integer customerPaymentPromptness
    ) {
        return CustomerGradeEvaluationDto.builder()
                .id(id)
                .userId(userId)
                .userType(userType)
                .reviewId(reviewId)
                .reservationId(reservationId)
                .customerCommunication(customerCommunication)
                .customerPunctuality(customerPunctuality)
                .customerManners(customerManners)
                .customerPaymentPromptness(customerPaymentPromptness)
                .build();
    }

}
