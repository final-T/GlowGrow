package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.Payment;
import lombok.Data;

@Data
public class PaymentSuccessDto {
    String mid; // 가맹점 Id -> tosspayments
    String version; // Payment 객체 응답 버전
    String paymentKey;
    String orderId;
    String orderName;
    String currency; // "KRW"
    String method; // 결제 수단
    String totalAmount;
    String balanceAmount;
    String suppliedAmount;
    String vat; // 부가가치세
    String status; // 결제 처리 상태
    String requestedAt;
    String approvedAt;
    String useEscrow; // false
    String cultureExpense; // false
    PaymentSuccessCardDto card; // 결제 카드 정보 (아래 자세한 정보 있음)
    String type; // 결제 타입 정보 (NOMAL / BILLING / CONNECTPAY)

    public static PaymentSuccessDto of(Payment payment) {
        PaymentSuccessDto dto = new PaymentSuccessDto();
        dto.setPaymentKey(payment.getPaymentKey());
        dto.setOrderId(payment.getPaymentId().toString());
        dto.setTotalAmount(payment.getAmount().toString());
        dto.setStatus(payment.isPaySuccessYN() ? "DONE" : "READY");
        // 나머지 필드들은 Payment 객체에서 가져올 수 있는 정보로 설정
        // 일부 정보는 Toss Payments API 응답에서 가져와야 할 수 있습니다
        return dto;
    }
}
