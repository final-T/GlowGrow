package com.tk.gg.users.application.dto;

import com.tk.gg.common.enums.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProviderGradeEvaluationDto(
        UUID id,
        Long userId,
        UserRole userType, // PROVIDER 또는 CUSTOMER,
        UUID reviewId,
        UUID reservationId,
        Integer providerServiceQuality,  // 서비스 품질
        Integer providerProfessionalism, // 전문성
        Integer providerCommunication,  // 의사소통
        Integer providerPunctuality,    // 시간 준수
        Integer providerPriceSatisfaction  // 가격 적정성
) {
    public static ProviderGradeEvaluationDto of(
            UUID id, Long userId, UserRole userType, UUID reviewId, UUID reservationId,
            Integer providerServiceQuality, Integer providerProfessionalism, Integer providerCommunication,
            Integer providerPunctuality, Integer providerPriceSatisfaction
    ) {
        return ProviderGradeEvaluationDto.builder()
                .id(id)
                .userId(userId)
                .userType(userType)
                .reviewId(reviewId)
                .reservationId(reservationId)
                .providerServiceQuality(providerServiceQuality)
                .providerProfessionalism(providerProfessionalism)
                .providerCommunication(providerCommunication)
                .providerPunctuality(providerPunctuality)
                .providerPriceSatisfaction(providerPriceSatisfaction)
                .build();
    }
}
