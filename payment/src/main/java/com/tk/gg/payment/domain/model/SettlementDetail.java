package com.tk.gg.payment.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.payment.domain.type.CouponType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_settlement_details")
public class SettlementDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID settlementDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false)
    private Long originalAmount; // 원래 결제 금액


    @Column(nullable = false)
    private Long amount; // 정산 금액

    @Column
    private UUID couponId; // 사용된 쿠폰 ID (없을 수 있음)

    @Enumerated(EnumType.STRING)
    @Column
    private CouponType couponType; // 사용된 쿠폰 타입 (없을 수 있음)



    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    @Builder(builderClassName = "CreateSettlementDetailBuilder", builderMethodName = "createSettlementDetailBuilder")
    public SettlementDetail(Settlement settlement, Payment payment, Long amount, Long originalAmount, UUID couponId, CouponType couponType) {
        this.settlement = settlement;
        this.payment = payment;
        this.amount = amount;
        this.isDeleted = false;
        this.couponId = couponId;
        this.couponType = couponType;
        this.originalAmount = originalAmount;
    }



}
