package com.tk.gg.promotion.infrastructure.messaging;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.CouponUser;
import com.tk.gg.promotion.infrastructure.repository.CouponRepository;
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "COUPON-ISSUE-CONSUMER")
@RequiredArgsConstructor
public class CouponIssueEventHandler {
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;

    @KafkaListener(topics = "coupon-issue", groupId = "coupon-issue-group")
    public void handleCouponIssueEvent(CouponIssueEvent couponIssueEvent) {
        log.info("발급 이벤트 수신 : {}", couponIssueEvent);
        // 발급하려는 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponIssueEvent.getCouponId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COUPON_NO_EXIST));

        // 쿠폰 사용자 생성
        CouponUser couponUser = CouponUser.builder()
                .coupon(coupon)
                .userId(couponIssueEvent.getUserId())
                .build();

        couponUserRepository.save(couponUser);
        log.info("발급 이벤트 처리 완료 : {}", couponIssueEvent);
    }
}
