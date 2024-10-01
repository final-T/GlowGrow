package com.tk.gg.promotion.application;

import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.application.dto.PromotionSearch;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.service.PromotionDomainService;
import com.tk.gg.promotion.application.dto.PromotionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionApplicationService {
    private final PromotionDomainService promotionDomainService;

    public PromotionResponseDto createPromotion(PromotionRequestDto requestDto) {
        // DTO -> 도메인 변환
        Promotion promotion = Promotion.builder()
                .postUserId(1L)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .status(requestDto.getStatus())
                .build();

        Promotion savedPromotion = promotionDomainService.createPromotion(promotion);

        return PromotionResponseDto.from(savedPromotion);
    }

    public PromotionResponseDto getPromotion(UUID promotionId) {
        Promotion promotion = promotionDomainService.getPromotion(promotionId);
        return PromotionResponseDto.from(promotion);
    }

    public PromotionResponseDto updatePromotion(UUID promotionId, PromotionRequestDto requestDto) {
        // DTO -> 도메인 변환
        Promotion promotion = Promotion.builder()
                .promotionId(promotionId)
                .postUserId(1L)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .status(requestDto.getStatus())
                .build();

        Promotion updatedPromotion = promotionDomainService.updatePromotion(promotion);

        return PromotionResponseDto.from(updatedPromotion);
    }

    public void deletePromotion(UUID promotionId) {
        promotionDomainService.deletePromotion(promotionId);
    }

    public Page<PromotionResponseDto> searchPromotions(Pageable pageable, PromotionSearch condition) {
        return promotionDomainService.searchPromotions(pageable, condition);
    }
}
