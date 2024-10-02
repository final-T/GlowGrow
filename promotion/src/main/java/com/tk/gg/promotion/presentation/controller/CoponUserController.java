package com.tk.gg.promotion.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.promotion.application.dto.CouponUserResponseDto;
import com.tk.gg.promotion.application.serivce.CouponApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "COUPON_USER_CONTROLLER")
@RequestMapping("/api/my-coupons")
public class CoponUserController {
    private final CouponApplicationService couponApplicationService;

    /**
     * 사용자 쿠폰 목록 조회
     * @return: 사용자 쿠폰 목록 조회 응답 DTO
     */
    @GetMapping
    public GlobalResponse<List<CouponUserResponseDto>> getUserCoupons() {
        // TODO : 헤더에 있는 사용자 ID로 사용자 쿠폰 목록 조회, 임시 값으로 1로 설정
        Long userId = 1L;
        List<CouponUserResponseDto> userCoupons = couponApplicationService.getUserCoupons(userId);
        return ApiUtils.success(ResponseMessage.COUPON_USER_RETRIEVE_SUCCESS.getMessage(), userCoupons);
    }
}
