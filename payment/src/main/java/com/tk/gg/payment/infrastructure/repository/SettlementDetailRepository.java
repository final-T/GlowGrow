package com.tk.gg.payment.infrastructure.repository;

import com.tk.gg.payment.domain.model.SettlementDetail;
import com.tk.gg.payment.domain.repository.SettlementDetailCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettlementDetailRepository extends JpaRepository<SettlementDetail, UUID> , SettlementDetailCustom {
    boolean existsByPayment_PaymentId(UUID paymentId);
}
