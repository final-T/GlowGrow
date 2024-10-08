package com.tk.gg.reservation.application.dto;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.domain.model.Grade;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record GradeDto(
        UUID id,
        Long userId,
        UserRole userType, // PROVIDER 또는 CUSTOMER
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

    public static GradeDto from(Grade entity) {
        return GradeDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userType(entity.getUserType())
                .providerServiceQuality(entity.getProviderServiceQuality() != null ? entity.getProviderServiceQuality() : 0)
                .providerProfessionalism(entity.getProviderProfessionalism() != null ? entity.getProviderProfessionalism() : 0)
                .providerCommunication(entity.getProviderCommunication() != null ? entity.getProviderCommunication() : 0)
                .providerPunctuality(entity.getProviderPunctuality() != null ? entity.getProviderPunctuality() : 0)
                .providerPriceSatisfaction(entity.getProviderPriceSatisfaction() != null ? entity.getProviderPriceSatisfaction() : 0)
                .customerCommunication(entity.getCustomerCommunication() != null ? entity.getCustomerCommunication() : 0)
                .customerPunctuality(entity.getCustomerPunctuality() != null ? entity.getCustomerPunctuality() : 0)
                .customerManners(entity.getCustomerManners() != null ? entity.getCustomerManners() : 0)
                .customerPaymentPromptness(entity.getCustomerPaymentPromptness() != null ? entity.getCustomerPaymentPromptness() : 0)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static GradeDto from(CustomerGradeDto dto){
        return GradeDto.builder()
                .id(dto.id())
                .userId(dto.userId())
                .userType(dto.userType())
                .customerCommunication(dto.customerCommunication())
                .customerPunctuality(dto.customerPunctuality())
                .customerManners(dto.customerManners())
                .customerPaymentPromptness(dto.customerPaymentPromptness())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }

    public static GradeDto from(ProviderGradeDto dto){
        return GradeDto.builder()
                .id(dto.id())
                .userId(dto.userId())
                .userType(dto.userType())
                .providerServiceQuality(dto.providerServiceQuality())
                .providerProfessionalism(dto.providerProfessionalism())
                .providerCommunication(dto.providerCommunication())
                .providerPunctuality(dto.providerPunctuality())
                .providerPriceSatisfaction(dto.providerPriceSatisfaction())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }
}
