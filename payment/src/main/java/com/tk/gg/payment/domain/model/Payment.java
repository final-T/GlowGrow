package com.tk.gg.payment.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.domain.type.CouponType;
import com.tk.gg.payment.domain.type.PaymentStatus;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Table(name = "p_payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", nullable = false, updatable = false)
    private UUID paymentId; // 결제 ID

    @Column(name = "pay_type")
    @Setter
    private String payType; // 결제 방식

    @Column(name = "amount", nullable = false)
    @Positive
    private Long amount; // 최종 결제 금액

    @Column(name = "original_amount", nullable = false)
    private Long originalAmount; // 할인 적용 전 원래 금액

    @Column(name = "discounted_amount")
    private Long discountedAmount = 0L; // 할인 후 금액

    @Column(name = "discount_amount")
    private Long discountAmount = 0L; // 할인 금액

    @Column(name = "coupon_type")
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId; // 예약 ID

    @Column(name = "coupon_id")
    @Setter
    private UUID couponId; // 쿠폰 ID

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // 결제 완료 시간

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // 결제 상태

    @Column(name = "order_name", nullable = false)
    private String orderName; // 결제명 PG

    @Column(nullable = false)
    private Boolean isDeleted = false;


    @Column(name = "customer_id")
    @Setter
    private Long customerId;

    @Setter
    private boolean paySuccessYN;

    @Column
    @Setter
    private String paymentKey;

    @Column
    @Setter
    private String failReason;

    @Column(name = "is_settled", nullable = false)
    private Boolean isSettled = false; // 정산 여부

    public void markAsSettled() {
        this.isSettled = true;
    }


    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Refund refund;

    public void paymentCancel(Refund refund) {
        this.status = PaymentStatus.REFUND;
        this.refund = refund;
    }

    public void changeStatusCompleted() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void changeStatusFailed() {
        this.status = PaymentStatus.FAILED;
    }

    public void changeStatusCanceled(){
        this.status = PaymentStatus.CANCELED;
    }



    public void updatePaidAt(String approvedAt) {
        this.paidAt = OffsetDateTime.parse(approvedAt).toLocalDateTime();
    }


    @Builder(builderClassName = "CreatePaymentBuilder", builderMethodName = "CreatePaymentBuilder")
    public Payment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo, Long customerId) {
        this.userId = authUserInfo.getId();
        this.createdBy = String.valueOf(authUserInfo.getId());
        this.customerId = customerId;
        this.originalAmount = 0L; // 처음 결제 데이터를 준비할 떄는 0
        //this.payType = requestDto.getPayType();
        this.amount = requestDto.getAmount();
        this.reservationId = requestDto.getReservationId();
        //this.couponId = requestDto.getCouponId();
        this.status = requestDto.getStatus();
        this.orderName = requestDto.getOrderName();
    }

    public void updateFromDto(PaymentRequestDto dto) {
        // 금액은 변경될 수 있음 (예: 쿠폰 적용)
        this.amount = dto.getAmount();
        this.discountAmount = dto.getDiscountAmount() != null ? dto.getDiscountAmount() : 0L;
        this.discountedAmount = dto.getAmount(); // 쿠폰 적용 시 할인 후 금액은 최종 결제 금액과 같음
        this.originalAmount = dto.getOriginalAmount();
        // 쿠폰 관련 정보 업데이트
        if (dto.getCouponId() != null && dto.getCouponcode() != null) {
            this.couponId = dto.getCouponId();

            // 쿠폰 타입 결정
            if (dto.getCouponcode().startsWith("GlowGrow-")) {
                this.couponType = CouponType.GLOWGROW;
            } else if (dto.getCouponcode().matches("\\d+-.*")) { // userId-UUID 형식 확인
                this.couponType = CouponType.PROVIDER;
            } else {
                // 예상치 못한 형식의 쿠폰 코드일 경우
                throw new IllegalArgumentException("Invalid coupon code format: " + dto.getCouponcode());
            }
        } else {
            // 쿠폰이 사용되지 않은 경우
            this.couponId = null;
            this.couponType = CouponType.NONE;
        }
    }
}
