package com.tk.gg.payment.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
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
    private Long amount;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    @Builder(builderClassName = "CreateSettlementDetailBuilder", builderMethodName = "createSettlementDetailBuilder")
    public SettlementDetail(Settlement settlement, Payment payment, Long amount) {
        this.settlement = settlement;
        this.payment = payment;
        this.amount = amount;
        this.isDeleted = false;
    }



}
