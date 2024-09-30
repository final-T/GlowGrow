package com.tk.gg.promotion.domain.service;

import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.infrastructure.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromotionDomainService {
    private final PromotionRepository promotionRepository;

    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }
}
