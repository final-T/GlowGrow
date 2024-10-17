package com.tk.gg.reservation.presentation.response;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.application.dto.GradeDto;
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

    public static GradeResponse from(GradeDto dto) {
        return GradeResponse.builder()
                .id(dto.id())
                .userId(dto.userId())
                .userType(dto.userType())
                .reviewId(dto.reviewId())
                .reservationId(dto.reservationId())
                .providerServiceQuality(dto.providerServiceQuality())
                .providerProfessionalism(dto.providerProfessionalism())
                .providerCommunication(dto.providerCommunication())
                .providerPunctuality(dto.providerPunctuality())
                .providerPriceSatisfaction(dto.providerPriceSatisfaction())
                .customerCommunication(dto.customerCommunication())
                .customerPunctuality(dto.customerPunctuality())
                .customerManners(dto.customerManners())
                .customerPaymentPromptness(dto.customerPaymentPromptness())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }
}
