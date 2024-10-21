package com.tk.gg.payment.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.payment.domain.model.*;
import com.tk.gg.payment.domain.repository.SettlementDetailCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SettlementDetailRepositoryImpl implements SettlementDetailCustom {
    private final JPAQueryFactory queryFactory;

    public SettlementDetailRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<SettlementDetail> findDetailsByProviderIdAndSettlementTime(Long providerId, Long settlementTime) {
        QSettlementDetail settlementDetail = QSettlementDetail.settlementDetail;
        QSettlement settlement = QSettlement.settlement;
        QPayment payment = QPayment.payment;
        QRefund refund = QRefund.refund;

        return queryFactory
                .selectFrom(settlementDetail)
                .join(settlementDetail.settlement, settlement).fetchJoin()
                .join(settlementDetail.payment, payment).fetchJoin()
                .leftJoin(payment.refund, refund).fetchJoin()
                .where(payment.userId.eq(providerId)
                        .and(settlement.settlementTime.eq(settlementTime))
                        .and(settlementDetail.isDeleted.eq(false)))
                .distinct()
                .fetch();
    }
}