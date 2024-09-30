package com.tk.gg.promotion.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.promotion.application.PromotionApplicationService;
import com.tk.gg.promotion.application.dto.PromotionCreateRequestDto;
import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "PROMOTION_CONTROLLER")
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionApplicationService promotionApplicationService;

    /**
     * 프로모션 생성
     * @param requestDto: 프로모션 생성 요청 DTO <br>
     *                  - title: 프로모션 제목  <br>
     *                  - description: 프로모션 설명 <br>
     *                  - startDate: 프로모션 시작일 <br>
     *                  - endDate: 프로모션 종료일 <br>
     *                  - status: 프로모션 상태 <br>
     *
     * @return: 프로모션 생성 응답 DTO
     */
    @PostMapping
    public GlobalResponse<PromotionResponseDto> createPromotion(@RequestBody PromotionCreateRequestDto requestDto) {
        PromotionResponseDto promotion = promotionApplicationService.createPromotion(requestDto);
        return ApiUtils.success(ResponseMessage.PROMOTION_CREATE_SUCCESS.getMessage(), promotion);
    }

}
