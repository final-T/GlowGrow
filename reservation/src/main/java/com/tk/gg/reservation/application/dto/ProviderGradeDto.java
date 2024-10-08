package com.tk.gg.reservation.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tk.gg.common.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProviderGradeDto(
        UUID id,
        Long userId,
        UserRole userType, // PROVIDER 또는 CUSTOMER
        // 제공자 (디자이너) 평가 항목들
        Integer providerServiceQuality,  // 서비스 품질
        Integer providerProfessionalism, // 전문성
        Integer providerCommunication,  // 의사소통
        Integer providerPunctuality,    // 시간 준수
        Integer providerPriceSatisfaction,  // 가격 적정성
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @QueryProjection
    public ProviderGradeDto(
            UUID id,
            Long userId,
            UserRole userType,
            Integer providerServiceQuality,
            Integer providerProfessionalism,
            Integer providerCommunication,
            Integer providerPunctuality,
            Integer providerPriceSatisfaction,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.userType = userType;
        this.providerServiceQuality = providerServiceQuality;
        this.providerProfessionalism = providerProfessionalism;
        this.providerCommunication = providerCommunication;
        this.providerPunctuality = providerPunctuality;
        this.providerPriceSatisfaction = providerPriceSatisfaction;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
