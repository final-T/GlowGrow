package com.tk.gg.payment.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.payment.domain.type.SettlementStatus;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_settlements")
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID settlementId;

    @Column(name = "provider_id", nullable = false)
    Long providerId;

    @Column(name = "total_amount",nullable = false)
    Long totalAmount;

    @Column(name = "settlement_time",nullable = false)
    Long settlementTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SettlementStatus status = SettlementStatus.PENDING;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementDetail> settlementDetails = new ArrayList<>();

    //소프트 삭제 메서드
    public void softDelete(AuthUserInfo authUserInfo){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = String.valueOf(authUserInfo.getId());
    }

    // 정산 상태 변경 메서드
    public void updateStatus(SettlementStatus newStatus, AuthUserInfo authUserInfo) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = String.valueOf(authUserInfo.getId());
    }

    // 총액 업데이트 메서드
    public void updateTotalAmount(Long newTotalAmount, AuthUserInfo authUserInfo) {
        this.totalAmount = newTotalAmount;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = String.valueOf(authUserInfo.getId());
    }

    @Builder(builderClassName = "CreateSettlementBuilder", builderMethodName = "createSettlementBuilder")
    public Settlement(Long providerId, Long totalAmount, Long settlementTime, AuthUserInfo authUserInfo) {
        this.providerId = providerId;
        this.totalAmount = totalAmount;
        this.settlementTime = settlementTime;
        this.status =  SettlementStatus.COMPLETED;
        this.createdBy = String.valueOf(authUserInfo.getId());
        this.updatedBy = String.valueOf(authUserInfo.getId());
    }

    @Builder(builderClassName = "UpdateSettlementBuilder", builderMethodName = "updateSettlementBuilder")
    public Settlement update(Long totalAmount, SettlementStatus status, AuthUserInfo authUserInfo) {
        if (totalAmount != null) {
            this.totalAmount = totalAmount;
        }
        if (status != null) {
            this.status = status;
        }
        this.updatedBy = String.valueOf(authUserInfo.getId());
        this.updatedAt = LocalDateTime.now();
        return this;
    }

}
