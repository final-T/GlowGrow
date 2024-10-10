package com.tk.gg.payment.infrastructure.repository;

import com.tk.gg.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPaymentIdAndIsDeletedFalse(UUID paymentId);
    default Optional<Payment> findByPaymentId(UUID paymentId) {
        return findByPaymentIdAndIsDeletedFalse(paymentId);
    }

    Optional<Payment> findByPaymentKeyAndUserId(String paymentKey, Long userId);
    default Optional<Payment> findByPaymentKey(String paymentKey, Long userId) {
        return findByPaymentKeyAndUserId(paymentKey, userId);
    }

    Optional<Payment> findByUserId(Long userId);

}
