package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.type.CouponType;
import com.tk.gg.payment.domain.type.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;
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
                .amount(payment.getAmount())
                .orderName(orderName)
                .customerEmail(customerEmail)
                .customerName(customerName)
                .successUrl(successUrl)
                .failUrl(failUrl)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private UUID paymentId;
        private String payType;
        private Long amount;
        private String orderName;
        private String customerEmail;
        private String createdAt;

        public static Detail from(Payment payment) {
            return Detail.builder()
                    .paymentId(payment.getPaymentId())
                    .payType(payment.getPayType().toString())
                    .amount(payment.getAmount())
                    .orderName(payment.getOrderName())
                    .createdAt(String.valueOf(payment.getPaidAt()))
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Get {
        private UUID paymentId;
        private String payType; // 결제 타입 - 카드/현금/포인트
        private UUID couponId; // 사용 쿠폰 ID
        private Boolean isSettled;
        private Long customerId;
        private String orderName; // 결제 상품명
        private LocalDateTime paidAt; // 결제 완료 시간
        private Boolean paySuccessyn; // 결제 성공 여부
        private String paymentKey; // 결제 키
        private UUID reservationId; // 예약 ID
        private PaymentStatus status; // 결제 상태
        private Long originalAmount; // 원래 가격
        private Long discountAmount; // 할인 가격
        private Long discountedAmount; // 할인 후 가격
        private Long amount; // 최종 결제 가격
        private CouponType couponType; // 쿠폰 타입
        private String failReason; // 실패 이유
        private boolean cancelYN; // 취소 YN
        private String cancelReason; // 취소 이유
        private String createdAt; // 결제가 이루어진 시간

        public Get(UUID paymentId, Long customerId, Long amount, String payType, PaymentStatus status, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.amount = amount;
            this.payType = payType;
            this.status = status;
            this.paidAt = paidAt;
            this.customerId = customerId;
        }


        public static Get from(Payment payment) {
            return Get.builder()
                    .paymentId(payment.getPaymentId())
                    .customerId(payment.getCustomerId())
                    .payType(payment.getPayType())
                    .couponId(payment.getCouponId())
                    .isSettled(payment.getIsSettled())
                    .orderName(payment.getOrderName())
                    .paidAt(payment.getPaidAt())
                    .paySuccessyn(payment.isPaySuccessYN())
                    .paymentKey(payment.getPaymentKey())
                    .reservationId(payment.getReservationId())
                    .status(payment.getStatus())
                    .originalAmount(payment.getOriginalAmount())
                    .discountAmount(payment.getDiscountAmount())
                    .discountedAmount(payment.getDiscountedAmount())
                    .amount(payment.getAmount())
                    .couponType(payment.getCouponType())
                    .failReason(payment.getFailReason())
                    .cancelYN(payment.getStatus() == PaymentStatus.CANCELED)
                    .cancelReason(payment.getStatus() == PaymentStatus.CANCELED ? payment.getFailReason() : null)
                    .createdAt(payment.getCreatedAt().toString())
                    .build();
        }
    }
}
