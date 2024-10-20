package com.tk.gg.payment.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.payment.application.dto.PaymentResponseDto;
import com.tk.gg.payment.application.dto.PaymentSearchCondition;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.QPayment;
import com.tk.gg.payment.domain.repository.PaymentRepositoryCustom;
import com.tk.gg.payment.domain.type.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.tk.gg.payment.domain.model.QPayment.payment;

@Repository
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PaymentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<Payment> findSettleablePayments(Long userId, LocalDateTime startDate, LocalDateTime endDate, PaymentStatus status) {
        QPayment payment = QPayment.payment;

        return queryFactory
                .selectFrom(payment)
                .where(payment.userId.eq(userId)
                        .and(payment.paidAt.between(startDate, endDate))
                        .and(payment.status.eq(status))
                        .and(payment.isSettled.eq(false)))
                .fetch();
    }

    @Override
    public Page<Payment> searchPaymentsByCondition(PaymentSearchCondition condition, Pageable pageable) {
        List<Payment> content = queryFactory
                .selectFrom(payment)
                .where(
                        customerIdEquals(condition.getCustomerId()),
                        amountBetween(condition.getMinAmount(), condition.getMaxAmount()),
                        statusEquals(condition.getStatus()),
                        payTypeEquals(condition.getPayType()),
                        paidAtBetween(
                                condition.getStartDate() != null ? condition.getStartDate().atStartOfDay() : null,
                                condition.getEndDate() != null ? condition.getEndDate().atTime(LocalTime.MAX) : null
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderBy(pageable))
                .fetch();

        long total = queryFactory
                .selectFrom(payment)
                .where(
                        customerIdEquals(condition.getCustomerId()),
                        amountBetween(condition.getMinAmount(), condition.getMaxAmount()),
                        statusEquals(condition.getStatus()),
                        payTypeEquals(condition.getPayType()),
                        paidAtBetween(
                                condition.getStartDate() != null ? condition.getStartDate().atStartOfDay() : null,
                                condition.getEndDate() != null ? condition.getEndDate().atTime(LocalTime.MAX) : null
                        )
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression customerIdEquals(Long customerId) {
        return customerId != null ? payment.customerId.eq(customerId) : null;
    }

    private BooleanExpression amountBetween(Long minAmount, Long maxAmount) {
        if (minAmount != null && maxAmount != null) {
            return payment.amount.between(minAmount, maxAmount);
        } else if (minAmount != null) {
            return payment.amount.goe(minAmount);
        } else if (maxAmount != null) {
            return payment.amount.loe(maxAmount);
        }
        return null;
    }

    private BooleanExpression statusEquals(PaymentStatus status) {
        return status != null ? payment.status.eq(status) : null;
    }

    private BooleanExpression payTypeEquals(String payType) {
        return payType != null ? payment.payType.eq(payType) : null;
    }

    private BooleanExpression paidAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return payment.paidAt.between(start, end);
        } else if (start != null) {
            return payment.paidAt.goe(start);
        } else if (end != null) {
            return payment.paidAt.loe(end);
        }
        return null;
    }

    private OrderSpecifier<?> getOrderBy(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return payment.paidAt.desc(); // 기본 정렬
        }

        for (Sort.Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "paidAt":
                    return order.isAscending() ? payment.paidAt.asc() : payment.paidAt.desc();
                case "amount":
                    return order.isAscending() ? payment.amount.asc() : payment.amount.desc();
                // 필요에 따라 다른 정렬 기준 추가
            }
        }

        return payment.paidAt.desc(); // 기본 정렬
    }
}
