package com.tk.gg.promotion.application.dto;

import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.PromotionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PromotionResponseDto {
    private Long postUserId;
    private UUID promotionId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private PromotionStatus status;

    public static PromotionResponseDto from(Promotion promotion) {
        return PromotionResponseDto.builder()
                .postUserId(promotion.getPostUserId())
                .promotionId(promotion.getPromotionId())
                .title(promotion.getTitle())
                .description(promotion.getDescription())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .status(promotion.getStatus())
                .build();
    }
}
