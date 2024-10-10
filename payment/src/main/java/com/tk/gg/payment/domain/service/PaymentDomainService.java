package com.tk.gg.payment.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.security.user.AuthUserInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentDomainService {
    public Payment createPayment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo) {
        return Payment.CreatePaymentBuilder()
                .authUserInfo(authUserInfo)
                .requestDto(requestDto)
                .build();

    }


    public void verifyPaymentAmount(Payment payment, Long amount) {
        if(!payment.getAmount().equals(amount)) {
            throw new GlowGlowException(GlowGlowError.PAYMENT_AMOUNT_MISMATCH);
        }
    }

    public void completePayment(Payment payment, String paymentKey, String approvedAt) {
        payment.setPaymentKey(paymentKey);
        payment.setPaySuccessYN(true);
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

}
