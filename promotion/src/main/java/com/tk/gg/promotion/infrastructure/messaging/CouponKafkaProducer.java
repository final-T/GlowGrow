package com.tk.gg.promotion.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "COUPON-ISSUE-PRODUCER")
@RequiredArgsConstructor
public class CouponKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCouponIssueEvent(CouponIssueEvent couponIssueEvent) {

        log.info("발급 이벤트 발행 : {}", couponIssueEvent);
        kafkaTemplate.send("coupon-issue", couponIssueEvent);
        log.info("발급 이벤트 발행 완료 : {}", couponIssueEvent);
    }
}
