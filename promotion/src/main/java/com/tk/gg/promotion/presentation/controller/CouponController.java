package com.tk.gg.promotion.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueResponseDto;
import com.tk.gg.promotion.application.dto.CouponResponseDto;
import com.tk.gg.promotion.application.serivce.CouponApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public GlobalResponse<CouponResponseDto> createCoupon(@RequestBody CouponCreateRequestDto requestDto) {
        // TODO : 권한 체크 (관리자, 서비스 제공자만 가능)
        CouponResponseDto coupon = couponApplicationService.createCoupon(requestDto);
        //return ApiUtils.success(ResponseMessage.COUPON_CREATE_SUCCESS.getMessage(), coupon);
        return ApiUtils.success("쿠폰 생성 성공", coupon);
    }

    @PostMapping("/issue")
    public GlobalResponse<CouponIssueResponseDto> issueCoupon(@RequestBody CouponIssueRequestDto requestDto) {
        // TODO : 권한 체크 (발급 대상자 유효성 검사 - 쿠폰 발급 대상자가 맞는지 확인)
        CouponIssueResponseDto couponIssueResponseDto = couponApplicationService.issueCoupon(requestDto);
        return ApiUtils.success(ResponseMessage.COUPON_USER_CREATE_SUCCESS.getMessage(), couponIssueResponseDto);
    }
}