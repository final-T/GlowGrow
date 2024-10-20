package com.tk.gg.payment.application.client;

import com.tk.gg.common.kafka.coupon.CouponUserResponseDto;
import com.tk.gg.payment.infrastructure.client.CouponFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponFeignClient couponFeignClient;

    @Override
    public List<CouponUserResponseDto> getUserCoupons(String authToken) {
        return couponFeignClient.getUserCoupons(authToken).getData();
    }

    @Override
    public void useCoupon(UUID couponId, Long userId) {
        couponFeignClient.useCoupon(couponId, userId);
    }


}
