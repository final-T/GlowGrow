package com.tk.gg.payment.domain.repository;

import com.tk.gg.payment.domain.model.SettlementDetail;

import java.util.List;

public interface SettlementDetailCustom {
    List<SettlementDetail> findDetailsByProviderIdAndSettlementTime(Long providerId, Long settlementTime);
}
