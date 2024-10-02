package com.tk.gg.promotion.application.serivce;

import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueResponseDto;
import com.tk.gg.promotion.application.dto.CouponResponseDto;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.service.CouponDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
