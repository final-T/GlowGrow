package com.tk.gg.reservation.application.dto;

import lombok.Builder;

@Builder
public record ResultGradeDto(
        Integer totalCount,
        Integer totalProviderServiceQuality,
        Integer totalProviderProfessionalism,
        Integer totalProviderCommunication,
        Integer totalProviderPunctuality,
        Integer totalProviderPriceSatisfaction,
        Integer totalCustomerCommunication,
        Integer totalCustomerPunctuality,
        Integer totalCustomerManners,
        Integer totalCustomerPaymentPromptness,
        Integer averageProviderServiceQuality,
        Integer averageProviderProfessionalism,
        Integer averageProviderCommunication,
        Integer averageProviderPunctuality,
        Integer averageProviderPriceSatisfaction,
        Integer averageCustomerCommunication,
        Integer averageCustomerPunctuality,
        Integer averageCustomerManners,
        Integer averageCustomerPaymentPromptness
) {
}
