package com.tk.gg.payment.application.scheduler;

import com.tk.gg.payment.application.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "SETTLEMENT-SCHEDULER")
@RequiredArgsConstructor
public class SettlementScheduler {

    private final SettlementService settlementService;


    /**
     * 정산 스케줄러
     * 매월 1일에 한달간의 결제금액을 정산합니다.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void runSettlement() {
        log.info("Starting settlement process");
        try {
            settlementService.processAutomaticSettlements();
            log.info("settlement process completed successfully");
        } catch (Exception e) {
            log.error("Error occurred during settlement process", e);
            // 여기에 알림 로직 추가 가능
        }
    }
}


