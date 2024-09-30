package com.tk.gg.promotion.infrastructure;

import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.repository.PromotionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID>, PromotionRepositoryCustom {
}
