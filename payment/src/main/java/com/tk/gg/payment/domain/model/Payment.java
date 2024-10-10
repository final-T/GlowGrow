package com.tk.gg.payment.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.domain.type.PayType;
import com.tk.gg.payment.domain.type.PaymentStatus;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", nullable = false, updatable = false)
    private UUID paymentId; // 결제 ID

    @Column(name = "pay_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType; // 결제 방식

    @Column(name = "amount", nullable = false)
    @Positive
    private Long amount; // 결제 금액

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId; // 예약 ID

    @Column(name = "coupon_id")
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

    @Setter
    private boolean paySuccessYN;

    @Column
    @Setter
    private String paymentKey;

    @Column
    @Setter
    private String failReason;


    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Refund refund;

    public void paymentCancel(Refund refund) {
        this.status = PaymentStatus.CANCELED;
        this.refund = refund;
    }

    public void changeStatusCompleted() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void changeStatusFailed() {
        this.status = PaymentStatus.FAILED;
    }



    public void updatePaidAt(String approvedAt) {
        this.paidAt = OffsetDateTime.parse(approvedAt).toLocalDateTime();
    }


    @Builder(builderClassName = "CreatePaymentBuilder", builderMethodName = "CreatePaymentBuilder")
    public Payment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo) {
        this.userId = authUserInfo.getId();
        this.createdBy = String.valueOf(authUserInfo.getId());
        this.payType = requestDto.getPayType();
        this.amount = requestDto.getAmount();
        this.reservationId = requestDto.getReservationId();
        this.couponId = requestDto.getCouponId();
        this.status = requestDto.getStatus();
        this.orderName = requestDto.getOrderName();
    }
}
