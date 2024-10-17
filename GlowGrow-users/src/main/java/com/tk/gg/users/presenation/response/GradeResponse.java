package com.tk.gg.users.presenation.response;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.users.application.dto.CustomerGradeEvaluationDto;
import com.tk.gg.users.application.dto.ProviderGradeEvaluationDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GradeResponse(
        UUID id,
        Long userId,
        UserRole userType, // PROVIDER 또는 CUSTOMER,
        UUID reviewId,
        UUID reservationId,
        // 제공자 (디자이너) 평가 항목들
        Integer providerServiceQuality,  // 서비스 품질
        Integer providerProfessionalism, // 전문성
        Integer providerCommunication,  // 의사소통
        Integer providerPunctuality,    // 시간 준수
        Integer providerPriceSatisfaction,  // 가격 적정성
        Integer customerCommunication,  // 고객 의사소통
        Integer customerPunctuality,  // 고객 시간 준수
        Integer customerManners,  // 고객 매너
        Integer customerPaymentPromptness,  // 결제 신속성
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProviderGradeEvaluationDto toProviderGradeEvaluationDto() {
        return ProviderGradeEvaluationDto.of(
                id, userId, userType, reviewId, reservationId,
                providerServiceQuality, providerProfessionalism, providerCommunication, providerPunctuality, providerPriceSatisfaction
        );
    }

    public CustomerGradeEvaluationDto toCustomerGradeEvaluationDto() {
        return CustomerGradeEvaluationDto.of(
                id, userId, userType, reviewId, reservationId,
                customerCommunication, customerPunctuality, customerManners, customerPaymentPromptness
        );
    }
}
