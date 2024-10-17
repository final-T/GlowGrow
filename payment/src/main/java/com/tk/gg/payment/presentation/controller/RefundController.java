package com.tk.gg.payment.presentation.controller;

import com.tk.gg.payment.application.dto.PaymentCancelDto;
import com.tk.gg.payment.application.service.RefundService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/toss/cancel")
    public ResponseEntity tossPaymentCancel(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestParam String paymentKey,
            @RequestParam String cancelReason
    ){
        PaymentCancelDto responseDto = refundService.cancelPayment(authUserInfo,paymentKey,cancelReason);
        return ResponseEntity.ok().body(responseDto);
    }
}
