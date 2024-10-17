package com.tk.gg.promotion.domain.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.application.dto.PromotionSearch;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@Service
@RequiredArgsConstructor
public class PromotionDomainService {
    private final PromotionRepository promotionRepository;

    @Transactional
    public Promotion createPromotion(Promotion promotion, AuthUserInfo userInfo) {
        return promotionRepository.save(promotion);
    }

    @Transactional(readOnly = true)
    public Promotion getPromotion(UUID promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new GlowGlowException(PROMOTION_NO_EXIST));
    }

    @Transactional
    public Promotion updatePromotion(Promotion promotion, AuthUserInfo userInfo) {
        Promotion savedPromotion = promotionRepository.findById(promotion.getPromotionId())
                .orElseThrow(() -> new GlowGlowException(PROMOTION_NO_EXIST));

        // 권한 체크 (본인이 생성한 프로모션만 마스터 계정만 수정 가능)
        verification(userInfo, savedPromotion);

        // 더티 체킹 업데이트
        savedPromotion.update(promotion);
        return savedPromotion;
    }

    @Transactional
    public void deletePromotion(UUID promotionId, AuthUserInfo userInfo) {
        // TODO: deletedBy 추가 필요
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new GlowGlowException(PROMOTION_NO_EXIST));
        
        // 권한 체크 (본인이 생성한 프로모션과 마스터 계정만 삭제 가능)
        verification(userInfo, promotion);

        promotionRepository.delete(promotion);
    }

    public Page<PromotionResponseDto> searchPromotions(Pageable pageable, PromotionSearch condition) {
        return promotionRepository.findPromotionsByCondition(condition, pageable);
    }

    private void verification(AuthUserInfo userInfo, Promotion savedPromotion) {
        // 본인 프로모션 이거나 마스터 계정인지 확인
        if (!Objects.equals(savedPromotion.getPostUserId(), userInfo.getId()) && userInfo.getUserRole() != UserRole.MASTER) {
            throw new GlowGlowException(AUTH_UNAUTHORIZED);
        }
    }
}