package com.tk.gg.promotion.application.serivce;

import com.tk.gg.promotion.application.dto.*;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.CouponUser;
import com.tk.gg.promotion.domain.service.CouponDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponApplicationService {
    private final CouponDomainService couponDomainService;

    // 쿠폰 생성
    public CouponResponseDto createCoupon(CouponCreateRequestDto requestDto) {
        Coupon savedCoupon = couponDomainService.createCoupon(requestDto);

        return CouponResponseDto.from(savedCoupon);
    }

    // 쿠폰 발급
    public CouponIssueResponseDto issueCoupon(CouponIssueRequestDto requestDto) {
        return couponDomainService.issueCoupoon(requestDto);
    }

    // 사용자 쿠폰 목록 조회
    public List<CouponUserResponseDto> getUserCoupons(Long userId) {
        List<CouponUser> userCoupons = couponDomainService.getUserCoupons(userId);

        return userCoupons.stream()
                .map(CouponUserResponseDto::from)
                .toList();

    }

    // 사용자 쿠폰 단건 조회
    public CouponUserResponseDto getUserCoupon(Long userId, UUID couponId) {
        CouponUser userCoupon = couponDomainService.getUserCoupon(userId, couponId);

        return CouponUserResponseDto.from(userCoupon);
    }
}
