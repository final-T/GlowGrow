package com.tk.gg.payment.application.client;

import com.tk.gg.common.kafka.coupon.CouponUserResponseDto;

import java.util.List;
import java.util.UUID;

public interface CouponService {
    List<CouponUserResponseDto> getUserCoupons(String authToken);

    void useCoupon(UUID couponId, Long userId);
}
