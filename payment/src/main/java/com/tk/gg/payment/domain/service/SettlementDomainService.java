package com.tk.gg.payment.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.SettlementDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.type.SettlementStatus;
import com.tk.gg.payment.infrastructure.repository.SettlementRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SETTLEMENT-DOMAIN-SERVICE")
public class SettlementDomainService {
    private final SettlementRepository settlementRepository;

    public Settlement createSettlement(Long providerId, List<Payment> payments, AuthUserInfo authUserInfo) {
        long totalAmount = calculateTotalAmount(payments);

        Settlement settlement = Settlement.createSettlementBuilder()
                .providerId(providerId)
                .totalAmount(totalAmount)
                .settlementTime(LocalDateTime.now())
                .authUserInfo(authUserInfo)
                .build();

        return settlementRepository.save(settlement);
    }

    public Settlement updateSettlement(UUID settlementId, SettlementDto.UpdateRequest requestDto, AuthUserInfo authUserInfo) {
        Settlement settlement = getSettlementById(settlementId);

        if (requestDto.getStatus() != null) {
            updateSettlementStatus(settlement, requestDto.getStatus(), authUserInfo);
        }
        if (requestDto.getTotalAmount() != null) {
            updateSettlementAmount(settlement, requestDto.getTotalAmount(), authUserInfo);
        }

        return settlementRepository.save(settlement);
    }

    public Page<SettlementDto.Response> searchSettlements(Pageable pageable, SettlementDto.SearchRequest requestDto) {
        return settlementRepository.findSettlementsByCondition(requestDto, pageable);
    }


    public Settlement getSettlementById(UUID settlementId) {
        return settlementRepository.findBySettlementId(settlementId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.SETTLEMENT_NO_EXIST));
    }

    public void updateSettlementStatus(Settlement settlement, SettlementStatus newStatus, AuthUserInfo authUserInfo) {
        settlement.updateStatus(newStatus, authUserInfo);
    }

    public void updateSettlementAmount(Settlement settlement, Long newAmount, AuthUserInfo authUserInfo) {
        settlement.updateTotalAmount(newAmount, authUserInfo);
    }

    public void deleteSettlement(UUID settlementId, AuthUserInfo authUserInfo) {
        Settlement settlement = getSettlementById(settlementId);
        settlement.softDelete(authUserInfo);
    }

    private long calculateTotalAmount(List<Payment> payments) {
        return payments.stream()
                .mapToLong(Payment::getAmount)
                .sum();
    }
}
