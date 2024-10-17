package com.tk.gg.payment.domain.service;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.model.SettlementDetail;
import com.tk.gg.payment.infrastructure.repository.SettlementDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementDetailDomainService {

    private final SettlementDetailRepository settlementDetailRepository;

    @Transactional
    public List<SettlementDetail> createSettlementDetails(Settlement settlement, List<Payment> payments) {
        List<SettlementDetail> newDetails = new ArrayList<>();
        for (Payment payment : payments) {
            if (!settlementDetailRepository.existsByPayment_PaymentId(payment.getPaymentId())) {
                SettlementDetail detail = SettlementDetail.createSettlementDetailBuilder()
                        .settlement(settlement)
                        .payment(payment)
                        .amount(payment.getAmount())
                        .build();
                newDetails.add(detail);
                log.info("Created new settlement detail for payment: {}", payment.getPaymentId());
            } else {
                log.warn("Payment {} is already settled. Skipping.", payment.getPaymentId());
            }
        }

        if (!newDetails.isEmpty()) {
            return settlementDetailRepository.saveAll(newDetails);
        } else {
            log.info("No new settlement details to create.");
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<SettlementDetail> getSettlementDetailsByProviderAndTime(Long providerId, Long settlementTime) {
        //return settlementDetailRepository.findByProviderIdAndSettlementTimeAndIsDeletedFalse(providerId, settlementTime);
        return settlementDetailRepository.findDetailsByProviderIdAndSettlementTime(providerId,settlementTime);
    }

}