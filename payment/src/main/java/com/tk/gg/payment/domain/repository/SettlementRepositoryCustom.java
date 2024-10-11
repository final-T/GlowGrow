package com.tk.gg.payment.domain.repository;

import com.tk.gg.payment.application.dto.SettlementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementRepositoryCustom {
    Page<SettlementDto.Response> findSettlementsByCondition(SettlementDto.SearchRequest condition, Pageable pageable);
}
