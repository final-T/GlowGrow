package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.type.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentSearchCondition {
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private PaymentStatus status;
    private String payType;
    private Long minAmount;
    private Long maxAmount;
}
