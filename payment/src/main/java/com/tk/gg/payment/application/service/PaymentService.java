package com.tk.gg.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.client.UserService;
import com.tk.gg.payment.application.dto.PaymentFailDto;
import com.tk.gg.payment.application.dto.PaymentRequestDto;
import com.tk.gg.payment.application.dto.PaymentSuccessDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.service.PaymentDomainService;
import com.tk.gg.payment.infrastructure.config.TossPaymentConfig;
import com.tk.gg.payment.infrastructure.repository.PaymentRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final UserService userService;
    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;


    @Transactional
    public Payment requestTossPayment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo) {
        boolean userExists = userService.isUserExistsByEmail(authUserInfo.getEmail());
        if (!userExists) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }

        Payment payment = paymentDomainService.createPayment(requestDto, authUserInfo);
        return paymentRepository.save(payment);
    }

    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = findPaymentByOrderId(orderId); // orderId == PaymentId

        paymentDomainService.verifyPaymentAmount(payment, amount);  // 요청 가격과 결제된 금액 비교


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        //JSONObject params = new JSONObject();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode params = objectMapper.createObjectNode();
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentSuccessDto result = null;
            // 최종 결제 승인 요청 URL : config에서 작성한 https://api.tosspayments.com/v1/payments/ + paymentKey
            result = restTemplate.postForObject(TossPaymentConfig.URL + paymentKey,
                    new HttpEntity<>(params,headers),
                    PaymentSuccessDto.class);
            // restTemplate.postForObject() -> Post 요청을 보내고 객체로 결과를 받는다.

        paymentDomainService.completePayment(payment, paymentKey, result.getApprovedAt());

        // 결제 정보 저장
        paymentRepository.save(payment);
        return result;
    }

    @Transactional
    public PaymentFailDto tossPaymentFail(String code, String message, String orderId) {
        Payment payment = findPaymentByOrderId(orderId);

        paymentDomainService.failPayment(payment, message);
        paymentRepository.save(payment);

        return PaymentFailDto.builder()
                .errorCode(code)
                .errorMessage(message)
                .orderId(orderId)
                .build();
    }


    private HttpHeaders getHeaders(){ // 요청 헤더에 꼭 Authorization 넣어줘야 함
        // 시크릿 키를 base64로 인코딩 한 값을 넣음
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder()
                        .encode((tossPaymentConfig.getSecretKey() + ":")
                                .getBytes(StandardCharsets.UTF_8)));

        // Basic Authorization 인가 코드를 보낼 때 시크릿 키를 Base64를 사용하여 인코딩하여 보내게 되는데,
        // 이 때, {시크릿 키 + ":"} 조합으로 인코딩해야한다.
        headers.set("Authorization", "Basic " + encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }


    // Payment 존재 여부 확인
    private Payment findPaymentByOrderId(String orderId) {
        return paymentRepository.findByPaymentId(UUID.fromString(orderId))
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_NO_EXIST));
    }
}
