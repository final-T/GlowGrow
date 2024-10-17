package com.tk.gg.payment.infrastructure.repository;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.repository.PaymentRepositoryCustom;
import com.tk.gg.payment.domain.type.PaymentStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> , PaymentRepositoryCustom {
    Optional<Payment> findByPaymentIdAndIsDeletedFalse(UUID paymentId);
    default Optional<Payment> findByPaymentId(UUID paymentId) {
        return findByPaymentIdAndIsDeletedFalse(paymentId);
    }

    Optional<Payment> findByPaymentKeyAndUserId(String paymentKey, Long userId);
    default Optional<Payment> findByPaymentKey(String paymentKey, Long userId) {
        return findByPaymentKeyAndUserId(paymentKey, userId);
    }

    @Query(value = "SELECT * FROM p_payments WHERE user_id = :userId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<Payment> findLatestByUserId(@Param("userId") Long userId);


    @Query(value = "SELECT DISTINCT user_id FROM  p_payments WHERE status = 'COMPLETED'", nativeQuery = true)
    List<Long> findDistinctUserIdsByStatusCompleted();
}
