package com.tk.gg.promotion.application.dto;

import com.tk.gg.promotion.domain.enums.PromotionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromotionSearch {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private PromotionStatus status;
}
