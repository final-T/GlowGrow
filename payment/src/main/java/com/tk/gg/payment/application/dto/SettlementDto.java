package com.tk.gg.payment.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.type.SettlementStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class SettlementDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        private Long providerId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Response {
        private UUID settlementId;
        private Long providerId;
        private Long totalAmount;
        private LocalDateTime settlementTime;
        private SettlementStatus status;

        @QueryProjection
        public Response(UUID settlementId, Long providerId, Long totalAmount, LocalDateTime settlementTime, SettlementStatus status) {
            this.settlementId = settlementId;
            this.providerId = providerId;
            this.totalAmount = totalAmount;
            this.settlementTime = settlementTime;
            this.status = status;
        }

        public static Response from(Settlement settlement) {
            return Response.builder()
                    .settlementId(settlement.getSettlementId())
                    .providerId(settlement.getProviderId())
                    .totalAmount(settlement.getTotalAmount())
                    .settlementTime(settlement.getSettlementTime())
                    .status(settlement.getStatus())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private SettlementStatus status;
        private Long totalAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchRequest {
        private Long providerId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private SettlementStatus status;
    }
}
