package com.tk.gg.payment.infrastructure.client;

import com.tk.gg.common.kafka.coupon.CouponUserResponseDto;
import com.tk.gg.common.response.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "promotion-service")
public interface CouponFeignClient {

    @GetMapping("/api/my-coupons")
    GlobalResponse<List<CouponUserResponseDto>> getUserCoupons(@RequestHeader("Authorization") String authToken);


    @PatchMapping("/api/my-coupons/{couponId}/use")
    GlobalResponse<Void> useCoupon(@PathVariable UUID couponId, @RequestBody Long userId);
}
