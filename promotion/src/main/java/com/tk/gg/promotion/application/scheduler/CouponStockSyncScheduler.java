package com.tk.gg.promotion.application.scheduler;

import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.infrastructure.repository.CouponRepository;
import com.tk.gg.promotion.infrastructure.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j(topic = "COUPON-STOCK-SYNC-SCHEDULER")
@RequiredArgsConstructor
public class CouponStockSyncScheduler {
    private final CouponRepository couponRepository;
    private final RedisRepository redisRepository;


    /**
     * 쿠폰 재고 동기화 스케줄러
     * 매일 자정에 Redis와 RDBMS의 쿠폰 재고 수량을 동기화합니다.
     * Redis에 저장된 쿠폰 재고 수량을 RDBMS에 저장합니다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void syncCouponStock() {
        List<Coupon> coupons = couponRepository.findAll();

        for (Coupon coupon : coupons) {
            String stockKey = "coupon:stock:" + coupon.getCouponId().toString();

            // Redis에 쿠폰 재고 수량을 조회
            String stockValue = redisRepository.get(stockKey);

            if (stockValue != null) {
                // Redis에 저장된 쿠폰 재고 수량을 RDBMS에 저장 (Integer로 변환)
                coupon.updateTotalQuantity(Integer.parseInt(stockValue));
                couponRepository.save(coupon);
            }
        }
    }
}
