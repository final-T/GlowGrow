package com.tk.gg.payment.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.domain.model.PendingPaymentRequest;
import com.tk.gg.payment.domain.repository.PendingPaymentRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PendingPaymentRequestDomainService {

    private final PendingPaymentRequestRepository pendingPaymentRequestRepository;

    public PendingPaymentRequest createPendingPaymentRequest(UUID reservationId, Long customerId, Long serviceProviderId, Integer price) {
        PendingPaymentRequest request = PendingPaymentRequest.create(reservationId, customerId, serviceProviderId, price);
        return pendingPaymentRequestRepository.save(request);
    }

    public List<PendingPaymentRequest> getPendingRequestsForProvider(Long providerId) {
        return pendingPaymentRequestRepository.findActiveByServiceProviderId(providerId);
    }

    @Transactional
    public void softDeletePendingRequest(UUID reservationId) {
        PendingPaymentRequest request = pendingPaymentRequestRepository.findActiveByReservationId(reservationId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_REQUEST_NOT_FOUND));
        request.softDelete();
        pendingPaymentRequestRepository.save(request);
    }

}
