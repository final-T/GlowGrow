package com.tk.gg.payment.domain.model;

import com.tk.gg.payment.domain.type.RefundType;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID refundId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_type")
    private RefundType refundType;

    @Column
    @Setter
    private boolean cancelYN;

    @Column
    private Long cancelAmount;

    @Setter
    @Column(name = "canceled_at", updatable = false)
    protected LocalDateTime canceledAt;

    @CreatedBy
    @Column(name = "canceled_by", updatable = false)
    protected String canceledBy;

    @Builder(builderClassName = "CreateRefundBuilder", builderMethodName = "CreateRefundBuilder")
    public Refund(Payment payment, String reason, boolean cancelYN, Long cancelAmount, AuthUserInfo authUserInfo, RefundType refundType) {
        this.payment = payment;
        this.reason = reason;
        this.refundType = refundType;
        this.cancelYN = cancelYN;
        this.cancelAmount = cancelAmount;
        this.canceledBy = String.valueOf(authUserInfo.getId());
    }

    public void updateRefundType(RefundType refundType) {
        this.refundType = refundType;
    }

}
