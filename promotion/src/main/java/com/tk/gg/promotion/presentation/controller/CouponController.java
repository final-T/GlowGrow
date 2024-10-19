package com.tk.gg.promotion.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.promotion.application.dto.*;
import com.tk.gg.promotion.application.serivce.CouponApplicationService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "COUPON_CONTROLLER")
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponApplicationService couponApplicationService;

    /**
     * 쿠폰 생성
     *
     * @param requestDto: 쿠폰 생성 요청 DTO
     * @return: 쿠폰 생성 응답 DTO
     */
    @PostMapping
    public GlobalResponse<CouponResponseDto> createCoupon(@AuthUser AuthUserInfo userInfo, @RequestBody CouponCreateRequestDto requestDto) {
        CouponResponseDto coupon = couponApplicationService.createCoupon(requestDto, userInfo);
        return ApiUtils.success(ResponseMessage.COUPON_CREATE_SUCCESS.getMessage(), coupon);
    }

    /**
     * 쿠폰 발급
     *
     * @param requestDto: 쿠폰 발급 요청 DTO
     * @return: 쿠폰 발급 응답 DTO
     */
    @PostMapping("/{couponId}/issue")
    public GlobalResponse<CouponIssueResponseDto> issueCoupon(@PathVariable UUID couponId, @RequestBody CouponIssueRequestDto requestDto) {
        CouponIssueResponseDto couponIssueResponseDto = couponApplicationService.issueCoupon(requestDto);
        return ApiUtils.success(ResponseMessage.COUPON_USER_CREATE_SUCCESS.getMessage(), couponIssueResponseDto);
    }
}