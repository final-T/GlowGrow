package com.tk.gg.payment.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.application.dto.PaymentResponseDto;
import com.tk.gg.payment.application.dto.PaymentSearchCondition;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.type.PaymentStatus;
import com.tk.gg.payment.infrastructure.repository.PaymentRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PAYMENT-DOMAIN-SERVICE")
public class PaymentDomainService {

    private final PaymentRepository paymentRepository;


    public Payment createPayment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo, Long customerId) {
        return Payment.CreatePaymentBuilder()
                .authUserInfo(authUserInfo)
                .requestDto(requestDto)
                .customerId(customerId)
                .build();

    }

    public void verifyPaymentAmount(Payment payment, Long amount) {
        if(!payment.getAmount().equals(amount)) {
            throw new GlowGlowException(GlowGlowError.PAYMENT_AMOUNT_MISMATCH);
        }
    }

    public void completePayment(Payment payment, String paymentKey, String approvedAt, String payType) {
        payment.setPaymentKey(paymentKey);
        payment.setPayType(payType);
        payment.setPaySuccessYN(true);
        payment.setFailReason(null); // 사용자가 취소했다가 다시 결제했을 경우 취소 사유 삭제
        payment.changeStatusCompleted();
        payment.updatePaidAt(approvedAt);
    }

    public void failPayment(Payment payment, String message) {
        payment.setPaySuccessYN(false);
        payment.setFailReason(message);
        payment.changeStatusFailed();
    }

    public void cancelPayment(Payment payment, String message) {
        payment.setPaySuccessYN(false);
        payment.setFailReason(message);
        payment.changeStatusCanceled();
    }

    public List<Payment> findPaymentsByProviderIdAndDateRange(Long providerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findSettleablePayments(providerId, startDate, endDate, PaymentStatus.COMPLETED);
        log.info("Found {} settleable payments for providerId: {}, startDate: {}, endDate: {}",
                payments.size(), providerId, startDate, endDate);
        return payments;
    }


    public List<Long> getAllProviderIds() {
        return paymentRepository.findDistinctUserIdsByStatusCompleted();
    }

    @Transactional
    public void markPaymentsAsSettled(List<Payment> payments) {
        payments.forEach(Payment::markAsSettled);
        paymentRepository.saveAll(payments);
    }


    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        List<Payment> payments = paymentRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        log.info("Retrieved {} payments for customerId: {}", payments.size(), customerId);
        return payments;
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponseDto.Get> searchPayments(PaymentSearchCondition condition, Pageable pageable) {
        Page<Payment> payments = paymentRepository.searchPaymentsByCondition(condition, pageable);
        if(payments.isEmpty()){
            throw new GlowGlowException(GlowGlowError.NO_SEARCH_RESULTS);
        }
        return payments.map(PaymentResponseDto.Get::from);
    }
}
