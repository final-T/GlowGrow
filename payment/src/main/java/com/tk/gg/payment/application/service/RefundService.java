package com.tk.gg.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.PaymentCancelDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Refund;
import com.tk.gg.payment.domain.service.RefundDomainService;
import com.tk.gg.payment.domain.type.RefundType;
import com.tk.gg.payment.infrastructure.config.TossPaymentConfig;
import com.tk.gg.payment.infrastructure.repository.RefundRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {

    private final PaymentService paymentService;
    private final RefundRepository refundRepository;
    private final RefundDomainService refundDomainService;

    @Transactional
    public PaymentCancelDto cancelPayment(AuthUserInfo authUserInfo, String paymentKey, String cancelReason) {
        Payment payment = paymentService.findPaymentByPaymentKey(paymentKey, authUserInfo.getId());

        if (payment.getRefund() != null) {
            throw new GlowGlowException(GlowGlowError.PAYMENT_ALREADY_CANCELED);
        }

        Refund refund = refundDomainService.createRefund(payment, cancelReason, payment.getAmount(), RefundType.REFUND_REQUESTED, authUserInfo);

        try {
            Map response = callTossApiForCancel(paymentKey, cancelReason);

            List<Map<String, Object>> cancels = (List<Map<String, Object>>) response.get("cancels");
            if (cancels != null && !cancels.isEmpty()) {
                Map<String, Object> cancelInfo = cancels.getFirst();
                String cancelStatus = (String) cancelInfo.get("cancelStatus");

                log.info("Cancel status: {}", cancelStatus);
                String canceledAtStr = (String) cancelInfo.get("canceledAt");
                LocalDateTime canceledAt = OffsetDateTime.parse(canceledAtStr).toLocalDateTime();

                refundDomainService.processRefundResponse(refund, cancelStatus, canceledAt);

                if (!refund.getRefundType().equals(RefundType.REFUND_COMPLETED)) {
                    log.warn("Refund failed. PaymentKey: {}, CancelStatus: {}", paymentKey, cancelStatus);
                }
            } else {
                log.warn("No cancel information found in the response. PaymentKey: {}", paymentKey);
                refund.updateRefundType(RefundType.REFUND_FAILED);
            }
        } catch (Exception e) {
            log.error("Error occurred while calling Toss API. PaymentKey: {}", paymentKey, e);
            refund.updateRefundType(RefundType.REFUND_FAILED);
            throw new GlowGlowException(GlowGlowError.REFUND_PROCESS_FAILED);
        }

        payment.paymentCancel(refund);
        refundRepository.save(refund);
        return PaymentCancelDto.from(payment, refund);
    }

    private Map callTossApiForCancel(String paymentKey, String cancelReason) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = paymentService.getHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode params = objectMapper.createObjectNode();
        params.put("cancelReason", cancelReason);

        return restTemplate.postForObject(TossPaymentConfig.URL + paymentKey + "/cancel",
                new HttpEntity<>(params, headers),
                Map.class
        );
    }
}