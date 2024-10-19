package com.tk.gg.promotion.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.promotion.application.dto.CouponUserResponseDto;
import com.tk.gg.promotion.application.serivce.CouponApplicationService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public GlobalResponse<List<CouponUserResponseDto>> getUserCoupons(@AuthUser AuthUserInfo userInfo) {
        // 토큰에 있는 사용자 ID로 사용자 쿠폰 조회
        Long userId = userInfo.getId();
        List<CouponUserResponseDto> userCoupons = couponApplicationService.getUserCoupons(userId);
        return ApiUtils.success(ResponseMessage.COUPON_USER_RETRIEVE_SUCCESS.getMessage(), userCoupons);
    }

    /**
     * 사용자 쿠폰 단건 조회
     */
    @GetMapping("/{couponId}")
    public GlobalResponse<CouponUserResponseDto> getUserCoupon(@PathVariable UUID couponId, @AuthUser AuthUserInfo userInfo) {
        // 토큰에 있는 사용자 ID로 사용자 쿠폰 조회
        Long userId = userInfo.getId();
        CouponUserResponseDto userCoupon = couponApplicationService.getUserCoupon(userId, couponId);

        return ApiUtils.success(ResponseMessage.COUPON_USER_RETRIEVE_SUCCESS.getMessage(), userCoupon);
    }

    /**
     * 사용자 쿠폰 사용
     * @param couponId: 쿠폰 ID
     * @return: 사용자 쿠폰 사용 응답 DTO
     */
    @PatchMapping("{couponId}/use")
    public GlobalResponse<Void> useCoupon(@PathVariable UUID couponId, @RequestBody Long userId) {
        // 쿠폰 사용 처리
        couponApplicationService.useCoupon(userId, couponId);
        return ApiUtils.success(ResponseMessage.COUPON_USER_USE_SUCCESS.getMessage(), null);
    }
}
