package com.tk.gg.reservation.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tk.gg.common.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerGradeDto(
        UUID id,
        Long userId,
        UserRole userType, // PROVIDER 또는 CUSTOMER
        // 고객 평가 항목들
        Integer customerCommunication,  // 고객 의사소통
        Integer customerPunctuality,  // 고객 시간 준수
        Integer customerManners,  // 고객 매너
        Integer customerPaymentPromptness,  // 결제 신속성
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @QueryProjection
    public CustomerGradeDto(
            UUID id,
            Long userId,
            UserRole userType,
            Integer customerCommunication,
            Integer customerPunctuality,
            Integer customerManners,
            Integer customerPaymentPromptness,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.userType = userType;
        this.customerCommunication = customerCommunication;
        this.customerPunctuality = customerPunctuality;
        this.customerManners = customerManners;
        this.customerPaymentPromptness = customerPaymentPromptness;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
