package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.Payment;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PaymentResponseDto {
    private UUID paymentId;
    private String payType; // 결제 타입 - 카드/현금/포인트
    private Long amount; // 가격 정보
    private String customerEmail; // 고객 이메일
    private String customerName; // 고객 이름
    private String successUrl; // 성공 시 리다이렉트 될 URL
    private String failUrl; // 실패 시 리다이렉트 될 URL
    private String orderName;
    private String failReason; // 실패 이유
    private boolean cancelYN; // 취소 YN
    private String cancelReason; // 취소 이유
    private String createdAt; // 결제가 이루어진 시간

    public static PaymentResponseDto of(Payment payment, String customerEmail, String customerName, String successUrl, String failUrl, String orderName) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .payType(payment.getPayType().toString())
                .amount(payment.getAmount())
                .orderName(orderName)
                .customerEmail(customerEmail)
                .customerName(customerName)
                .successUrl(successUrl)
                .failUrl(failUrl)
                .build();
    }
}
