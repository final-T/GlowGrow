package com.tk.gg.payment.domain.repository;

import com.tk.gg.payment.domain.model.PendingPaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PendingPaymentRequestRepository extends JpaRepository<PendingPaymentRequest, Long> {

    @Query("SELECT p FROM PendingPaymentRequest p WHERE p.serviceProviderId = :serviceProviderId AND p.isDeleted = false")
    List<PendingPaymentRequest> findActiveByServiceProviderId(@Param("serviceProviderId") Long serviceProviderId);

    @Query("SELECT p FROM PendingPaymentRequest p WHERE p.reservationId = :reservationId AND p.isDeleted = false")
    Optional<PendingPaymentRequest> findActiveByReservationId(@Param("reservationId") UUID reservationId);
}


