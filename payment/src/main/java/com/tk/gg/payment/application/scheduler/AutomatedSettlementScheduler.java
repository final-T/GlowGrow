package com.tk.gg.payment.application.scheduler;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.service.PaymentDomainService;
import com.tk.gg.payment.domain.service.SettlementDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j(topic = "AUTOMATED-SETTLEMENT-SCHEDULER")
@RequiredArgsConstructor
public class AutomatedSettlementScheduler {

    private final PaymentDomainService paymentDomainService;
    private final SettlementDomainService settlementDomainService;

    // 테스트용 1분 주기 스케줄러
    @Scheduled(cron = "0 * * * * ?")
    public void runTestSettlement() {
        log.info("Starting test settlement process");
        runMonthlySettlement();
    }

    //@Scheduled(cron = "0 0 1 * * ?") // 매월 1일 00:00에 실행
    public void runMonthlySettlement() {
        log.info("Starting automated monthly settlement process");
        try {
            LocalDate today = LocalDate.now();
            LocalDate lastMonth = today.minusMonths(1);
            LocalDateTime startDate = lastMonth.withDayOfMonth(1).atStartOfDay(); // 지난 달의 첫 날 00:00:00
            LocalDateTime endDate = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()).atTime(LocalTime.MAX); // 지난 달의 마지막 날 23:59:59.999999999

            Long settlementTime = lastMonth.getYear() * 100L + lastMonth.getMonthValue(); // YYYYMM 형식


            log.info("Settlement period - Start: {}, End: {}",
                    startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));



            List<Long> allProviderIds = paymentDomainService.getAllProviderIds();

            for (Long providerId : allProviderIds) {
                processSettlementForProvider(providerId, startDate, endDate, settlementTime);
            }

            log.info("Automated monthly settlement process completed successfully");
        } catch (Exception e) {
            log.error("Error occurred during automated monthly settlement process", e);
            // 여기에 알림 로직 추가 가능
        }
    }

    private void processSettlementForProvider(Long providerId, LocalDateTime startDate, LocalDateTime endDate, Long settlementTime) {
        try {
            List<Payment> payments = paymentDomainService.findPaymentsByProviderIdAndDateRange(
                    providerId, startDate, endDate
            );

            if (!payments.isEmpty()) {
                Settlement settlement = settlementDomainService.createAutomatedSettlement(providerId, payments, settlementTime);
                log.info("Automated settlement created for provider: {}, amount: {}", providerId, settlement.getTotalAmount());
            } else {
                log.info("No payments found for provider: {} in the period", providerId);
            }
        } catch (Exception e) {
            log.error("Error processing automated settlement for provider: {}", providerId, e);
        }
    }
}

