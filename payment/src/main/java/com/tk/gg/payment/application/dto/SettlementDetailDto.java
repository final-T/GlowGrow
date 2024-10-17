package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.SettlementDetail;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class SettlementDetailDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID settlementId;
        private UUID paymentId;
        private Long amount;

        // Payment의 모든 정보가 아닌, 필요한 정보만을 제공하기위해 Dto로 받는다.
        private PaymentResponseDto.Detail payment;

        public static Response from(SettlementDetail settlementDetail) {
            return Response.builder()
                    .settlementId(settlementDetail.getSettlementDetailId())
                    .paymentId(settlementDetail.getPayment().getPaymentId())
                    .amount(settlementDetail.getAmount())
                    .payment(PaymentResponseDto.Detail.from(settlementDetail.getPayment())) // 지연 로딩
                    .build();
        }
    }
}