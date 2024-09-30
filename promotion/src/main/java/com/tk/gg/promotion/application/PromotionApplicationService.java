package com.tk.gg.promotion.application;

import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.service.PromotionDomainService;
import com.tk.gg.promotion.application.dto.PromotionCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionApplicationService {
    private final PromotionDomainService promotionDomainService;

    public PromotionResponseDto createPromotion(PromotionCreateRequestDto requestDto) {
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
}
