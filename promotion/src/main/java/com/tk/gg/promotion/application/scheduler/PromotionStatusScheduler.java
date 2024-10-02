package com.tk.gg.promotion.application.scheduler;

import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PromotionStatusScheduler {
    private final PromotionRepository promotionRepository;

    // 매일 자정에 실행
    // 활성화 기간이 지난 프로모션을 비활성화로 변경
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updatePromotionStatus() {
        log.info("스케줄러 시작 - 기간이 지난 프로모션을 INACTIVE로 변경합니다.");
        promotionRepository.updatePromotionStatus();
        log.info("스케줄러 종료 - 상태 변경 완료.");
    }
}