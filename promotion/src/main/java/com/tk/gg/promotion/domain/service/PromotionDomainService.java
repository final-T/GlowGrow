package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.application.dto.PromotionSearch;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.infrastructure.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Promotion updatePromotion(Promotion promotion) {
        // TODO: 회원 도메인 추가 후 본인 확인 로직 추가 필요.
        Promotion savedPromotion = promotionRepository.findById(promotion.getPromotionId())
                .orElseThrow(() -> new GlowGlowException(PROMOTION_NO_EXIST));
        // 더티 체킹 업데이트
        savedPromotion.update(promotion);
        return savedPromotion;
    }

    @Transactional
    public void deletePromotion(UUID promotionId) {
        // TODO: 회원 도메인 추가 후 본인 확인 로직 추가 필요. deletedBy 추가 필요

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new GlowGlowException(PROMOTION_NO_EXIST));
        promotionRepository.delete(promotion);
    }

    public Page<PromotionResponseDto> searchPromotions(Pageable pageable, PromotionSearch condition) {
        return promotionRepository.findPromotionsByCondition(condition, pageable);
    }
}
