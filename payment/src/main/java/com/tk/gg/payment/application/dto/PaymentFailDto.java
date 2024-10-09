package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailDto {
    String errorCode;
    String errorMessage;
    String orderId;
}
