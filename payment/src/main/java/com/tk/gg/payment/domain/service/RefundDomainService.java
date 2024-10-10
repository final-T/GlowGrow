package com.tk.gg.payment.domain.service;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Refund;
import com.tk.gg.payment.domain.type.RefundType;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundDomainService {

    private static final String CANCEL_STATUS_DONE = "DONE";

    public Refund createRefund(Payment payment, String cancelReason, long cancelAmount, RefundType refundType, AuthUserInfo authUserInfo) {
        return Refund.CreateRefundBuilder()
                .payment(payment)
                .authUserInfo(authUserInfo)
                .reason(cancelReason)
                .refundType(refundType)
                .cancelYN(false)
                .cancelAmount(cancelAmount)
                .build();
    }

    public void processRefundResponse(Refund refund, String cancelStatus, LocalDateTime canceledAt) {
        if (CANCEL_STATUS_DONE.equals(cancelStatus)) {
            refund.updateRefundType(RefundType.REFUND_COMPLETED);
            refund.setCancelYN(true);
            refund.setCanceledAt(canceledAt);
        } else {
            refund.updateRefundType(RefundType.REFUND_FAILED);
        }
    }
}