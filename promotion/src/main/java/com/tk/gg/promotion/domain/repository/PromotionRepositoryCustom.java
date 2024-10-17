package com.tk.gg.promotion.domain.repository;

import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.application.dto.PromotionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionRepositoryCustom {
    Page<PromotionResponseDto> findPromotionsByCondition(PromotionSearch condition, Pageable pageable);
}
