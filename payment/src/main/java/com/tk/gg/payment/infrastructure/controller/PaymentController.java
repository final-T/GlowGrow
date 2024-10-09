package com.tk.gg.payment.infrastructure.controller;//package com.tk.gg.payment.infrastructure.controller;

import com.tk.gg.payment.application.service.PaymentService;
import com.tk.gg.payment.application.dto.PaymentFailDto;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.application.dto.PaymentResponseDto;
import com.tk.gg.payment.application.dto.PaymentSuccessDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.infrastructure.config.TossPaymentConfig;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentService paymentService;

    @PostMapping("/toss")
    public ResponseEntity requestTossPayment(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestBody @Valid PaymentRequestDto requestDto
            ) {
        Payment payment = paymentService.requestTossPayment(requestDto, authUserInfo);

        String successUrl = requestDto.getYourSuccessUrl() != null ? requestDto.getYourSuccessUrl() : tossPaymentConfig.getSuccessUrl();
        String failUrl = requestDto.getYourFailUrl() != null ? requestDto.getYourFailUrl() : tossPaymentConfig.getFailUrl();

        PaymentResponseDto responseDto = PaymentResponseDto.of(
                payment,
                authUserInfo.getEmail(),
                requestDto.getOrderName(),
                authUserInfo.getUsername(),
                successUrl,
                failUrl
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/toss/success")
    public ResponseEntity tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ){
        PaymentSuccessDto responseDto = paymentService.tossPaymentSuccess(paymentKey,orderId,amount);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/toss/fail")
    public ResponseEntity tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ){
        PaymentFailDto responseDto = paymentService.tossPaymentFail(code,message,orderId);
        return ResponseEntity.ok().body(responseDto);
    }


}
