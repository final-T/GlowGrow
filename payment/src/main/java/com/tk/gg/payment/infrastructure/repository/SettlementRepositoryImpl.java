package com.tk.gg.payment.infrastructure.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.payment.application.dto.QSettlementDto_Response;
import com.tk.gg.payment.application.dto.SettlementDto;
import com.tk.gg.payment.domain.model.QSettlement;
import com.tk.gg.payment.domain.repository.SettlementRepositoryCustom;
import com.tk.gg.payment.domain.type.SettlementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SettlementDto.Response> findSettlementsByCondition(SettlementDto.SearchRequest condition, Pageable pageable) {
        QSettlement settlement = QSettlement.settlement;

        JPAQuery<SettlementDto.Response> query = queryFactory
                .select(new QSettlementDto_Response(
                        settlement.settlementId,
                        settlement.providerId,
                        settlement.totalAmount,
                        settlement.settlementTime,
                        settlement.status
                ))
                .from(settlement)
                .where(
                        providerIdEq(condition.getProviderId()),
                        settlementTimeEq(condition.getSettlementTime()),   // settlementTime으로 검색
                        settlement.isDeleted.isFalse()
                );

        // 정렬 및 페이징
        long total = query.fetchCount();
        List<SettlementDto.Response> settlements = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(settlements, pageable, total);
    }

    private BooleanExpression providerIdEq(Long providerId) {
        return providerId != null ? QSettlement.settlement.providerId.eq(providerId) : null;
    }

    private BooleanExpression settlementTimeEq(Long settlementTime) {
        return settlementTime != null ? QSettlement.settlement.settlementTime.eq(settlementTime) : null;
    }
}