package com.tk.gg.promotion.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.promotion.application.serivce.PromotionApplicationService;
import com.tk.gg.promotion.application.dto.PromotionRequestDto;
import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.application.dto.PromotionSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public GlobalResponse<PromotionResponseDto> createPromotion(@RequestBody PromotionRequestDto requestDto) {
        PromotionResponseDto promotion = promotionApplicationService.createPromotion(requestDto);
        return ApiUtils.success(ResponseMessage.PROMOTION_CREATE_SUCCESS.getMessage(), promotion);
    }

    /**
     * 프로모션 단건 조회
     * @param promotionId: 프로모션 ID
     * @return: 프로모션 단건 조회 응답 DTO
     */
    @GetMapping("/{promotionId}")
    public GlobalResponse<PromotionResponseDto> getPromotion(@PathVariable("promotionId") UUID promotionId) {
        PromotionResponseDto promotion = promotionApplicationService.getPromotion(promotionId);
        return ApiUtils.success(ResponseMessage.PROMOTION_RETRIEVE_SUCCESS.getMessage(), promotion);
    }

    /**
     * 프로모션 수정
     * @param promotionId: 프로모션 ID
     * @param requestDto: 프로모션 수정 요청 DTO <br>
     *                  - title: 프로모션 제목  <br>
     *                  - description: 프로모션 설명 <br>
     *                  - startDate: 프로모션 시작일 <br>
     *                  - endDate: 프로모션 종료일 <br>
     *                  - status: 프로모션 상태 <br>
     *
     * @return: 프로모션 수정 응답 DTO
     */
    @PutMapping("/{promotionId}")
    public GlobalResponse<PromotionResponseDto> updatePromotion(@PathVariable("promotionId") UUID promotionId,
                                                                @RequestBody PromotionRequestDto requestDto) {
        PromotionResponseDto promotion = promotionApplicationService.updatePromotion(promotionId, requestDto);
        return ApiUtils.success(ResponseMessage.PROMOTION_UPDATE_SUCCESS.getMessage(), promotion);
    }

    /**
     * 프로모션 삭제
     * @param promotionId: 프로모션 ID
     * @return: 프로모션 삭제 응답 DTO
     */
    @DeleteMapping("/{promotionId}")
    public GlobalResponse<UUID> deletePromotion(@PathVariable("promotionId") UUID promotionId) {
        promotionApplicationService.deletePromotion(promotionId);
        return ApiUtils.success(ResponseMessage.PROMOTION_DELETE_SUCCESS.getMessage(), promotionId);
    }

    /**
     * 프로모션 검색 및 페이징 조회
     * @param pageable: 페이징 정보
     *                - page: 페이지 번호 <br>
     *                - size: 페이지 크기 <br>
     *                - sort: 정렬 정보 <br>
     * @param condition: 프로모션 검색 조건
     *                 - title: 프로모션 제목 <br>
     *                 - status: 프로모션 상태 <br>
     *                 - startDate: 프로모션 시작일 <br>
     *                 - endDate: 프로모션 종료일 <br>
     * @return: 프로모션 검색 및 페이징 조회 응답 DTO
     */
    @GetMapping
    public GlobalResponse<Page<PromotionResponseDto>> searchPromotions(
            Pageable pageable,
            PromotionSearch condition
    ) {
        Page<PromotionResponseDto> promotionResponseDtos = promotionApplicationService.searchPromotions(pageable, condition);
        return ApiUtils.success(ResponseMessage.PROMOTION_RETRIEVE_SUCCESS.getMessage(), promotionResponseDtos);
    }
}
