package com.tk.gg.payment.domain.repository;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.type.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepositoryCustom {
    List<Payment> findSettleablePayments(Long userId, LocalDateTime startDate, LocalDateTime endDate, PaymentStatus paymentStatus);
}
