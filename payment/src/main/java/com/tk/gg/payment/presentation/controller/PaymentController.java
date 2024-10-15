package com.tk.gg.payment.presentation.controller;//package com.tk.gg.payment.infrastructure.controller;

import com.tk.gg.payment.application.dto.PaymentFailDto;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.application.dto.PaymentResponseDto;
import com.tk.gg.payment.application.dto.PaymentSuccessDto;
import com.tk.gg.payment.application.service.PaymentService;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.infrastructure.config.TossPaymentConfig;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentService paymentService;

    @GetMapping("/prepare")
    public ModelAndView preparePayment() {
        // payment-prepare 페이지로 이동
        ModelAndView modelAndView = new ModelAndView("payment-prepare");
        return modelAndView;
    }

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
    public ModelAndView tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ){
        PaymentSuccessDto responseDto = paymentService.tossPaymentSuccess(paymentKey,orderId,amount);

        try {
            LocalDateTime approvedAtDateTime = LocalDateTime.parse(responseDto.getApprovedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String formattedDate = approvedAtDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            responseDto.setApprovedAt(formattedDate);
        } catch (Exception e) {
            log.error("Error parsing date: " + responseDto.getApprovedAt(), e);
            // 에러 발생 시 원본 문자열 유지
        }

        ModelAndView modelAndView = new ModelAndView("payment-success");
        modelAndView.addObject("payment", responseDto);
        return modelAndView;
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

    @GetMapping("/toss/fail-cancel")
    public ResponseEntity tossPaymentUserCancel(
            @RequestParam String code,
            @RequestParam String message,
            @AuthUser AuthUserInfo authUserInfo
    ){
        paymentService.tossPaymentUserCancel(code,message,authUserInfo);
        return ResponseEntity.ok().body(code + " " + message);
    }

    @Value("${payment.toss.client-key}")
    private String tossClientKey;

    @GetMapping("/client-key")
    public ResponseEntity<String> getTossClientKey() {
        return ResponseEntity.ok(tossClientKey);
    }


}
