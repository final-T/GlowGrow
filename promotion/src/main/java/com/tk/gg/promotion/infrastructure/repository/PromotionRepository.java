package com.tk.gg.promotion.infrastructure.repository;

import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.repository.PromotionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID>, PromotionRepositoryCustom {
    @Modifying
    @Query("UPDATE Promotion p SET p.status = 'INACTIVE' " +
            "WHERE p.status = 'ACTIVE' AND p.endDate < CURRENT_DATE")
    void updatePromotionStatus();
}