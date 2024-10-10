package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Refund;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PaymentCancelDto {
    private UUID refundId;
    private UUID paymentId;
    private String paymentKey;
    private String cancelReason;
    private Long cancelAmount;
    private String cancelStatus;

    public static PaymentCancelDto from(Payment payment, Refund refund) {
        return PaymentCancelDto.builder()
                .refundId(refund.getRefundId())
                .paymentId(payment.getPaymentId())
                .paymentKey(payment.getPaymentKey())
                .cancelReason(refund.getReason())
                .cancelAmount(refund.getCancelAmount())
                .cancelStatus(String.valueOf(refund.getRefundType()))
                .build();
    }
}
