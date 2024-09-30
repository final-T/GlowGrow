package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.infrastructure.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@Service
@RequiredArgsConstructor
public class PromotionDomainService {
    private final PromotionRepository promotionRepository;

    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Transactional(readOnly = true)
    public Promotion getPromotion(UUID promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new GlowGlowException(PROMOTION_NO_EXIST));
    }
}
