package com.tk.gg.payment.infrastructure.repository;

import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.repository.SettlementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SettlementRepository extends JpaRepository<Settlement, UUID>, SettlementRepositoryCustom {
    Optional<Settlement> findBySettlementIdAndIsDeletedFalse(UUID settlementId);
    default Optional<Settlement> findBySettlementId(UUID settlementId) {
        return findBySettlementIdAndIsDeletedFalse(settlementId);
    }
}
