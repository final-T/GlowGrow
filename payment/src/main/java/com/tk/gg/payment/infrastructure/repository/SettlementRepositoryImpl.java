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
                        settlementTimeBetween(condition.getStartDate(), condition.getEndDate()),
                        statusEq(condition.getStatus()),
                        settlement.isDeleted.isFalse()
                );

        // 정렬 적용
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                switch (order.getProperty()) {
                    case "settlementTime":
                        query.orderBy(order.isAscending() ? settlement.settlementTime.asc() : settlement.settlementTime.desc());
                        break;
                    case "totalAmount":
                        query.orderBy(order.isAscending() ? settlement.totalAmount.asc() : settlement.totalAmount.desc());
                        break;
                    // 필요한 다른 정렬 조건들을 여기에 추가
                }
            }
        }

        // 전체 카운트 조회
        long total = query.fetchCount();

        // 페이징 적용 및 결과 조회
        List<SettlementDto.Response> settlements = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(settlements, pageable, total);
    }

    private BooleanExpression providerIdEq(Long providerId) {
        return providerId != null ? QSettlement.settlement.providerId.eq(providerId) : null;
    }

    private BooleanExpression settlementTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return QSettlement.settlement.settlementTime.between(startDate, endDate);
        } else if (startDate != null) {
            return QSettlement.settlement.settlementTime.goe(startDate);
        } else if (endDate != null) {
            return QSettlement.settlement.settlementTime.loe(endDate);
        }
        return null;
    }

    private BooleanExpression statusEq(SettlementStatus status) {
        return status != null ? QSettlement.settlement.status.eq(status) : null;
    }
}