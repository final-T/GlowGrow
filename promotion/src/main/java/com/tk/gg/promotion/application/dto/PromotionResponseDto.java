package com.tk.gg.promotion.application.dto;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public PromotionResponseDto(Long postUserId, UUID promotionId, String title, String description, LocalDate startDate, LocalDate endDate, PromotionStatus status) {
        this.postUserId = postUserId;
        this.promotionId = promotionId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

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
