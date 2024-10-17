package com.tk.gg.payment.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.QPayment;
import com.tk.gg.payment.domain.repository.PaymentRepositoryCustom;
import com.tk.gg.payment.domain.type.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
}
