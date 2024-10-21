package com.tk.gg.payment.presentation.controller;//package com.tk.gg.payment.infrastructure.controller;

import com.tk.gg.common.kafka.coupon.CouponUserResponseDto;
import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.payment.application.dto.*;
import com.tk.gg.payment.application.service.PaymentService;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.PendingPaymentRequest;
import com.tk.gg.payment.infrastructure.config.TossPaymentConfig;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentService paymentService;

    @PostMapping("/prepare")
    public ResponseEntity<UUID> requestPaymentFromProvider(
            @RequestBody @Valid PaymentRequestDto requestDto,
            @AuthUser AuthUserInfo authUserInfo
            ){
        UUID paymentPrepareId = paymentService.preparePayment(requestDto, authUserInfo);
        return ResponseEntity.ok(paymentPrepareId);
    }


    @GetMapping("/prepare/{paymentId}")
    public ModelAndView showPaymentPrepare(@PathVariable UUID paymentId) {
        PaymentRequestDto preparedPayment = paymentService.getPreparedPayment(paymentId);
        String clientKey = paymentService.getTossClientKey();

        // payment-prepare 페이지로 이동
        ModelAndView modelAndView = new ModelAndView("payment-prepare");
        modelAndView.addObject("preparedPayment", preparedPayment);
        modelAndView.addObject("clientKey", clientKey);
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

    // 사용자 취소
    @GetMapping("/toss/fail-cancel")
    public ResponseEntity tossPaymentUserCancel(
            @RequestParam String code,
            @RequestParam String message,
            @AuthUser AuthUserInfo authUserInfo
    ){
        paymentService.tossPaymentUserCancel(code,message,authUserInfo);
        return ResponseEntity.ok().body(code + " " + message);
    }

    /**
     * 내 결제 목록 조회
     * @return:
     */
    @GetMapping("/myPayment")
    @ResponseBody
    public GlobalResponse<List<PaymentResponseDto.Get>> getPayments(@AuthUser AuthUserInfo authUserInfo){
        List<PaymentResponseDto.Get> responseDto = paymentService.getPayments(authUserInfo);

        return ApiUtils.success(ResponseMessage.PAYMENT_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    /**
     * 결제 검색 API
     *
     * @param authUserInfo 인증된 사용자 정보
     * @param pageable 페이징 정보. 정렬 조건을 포함합니다.
     *                 정렬 가능한 필드:
     *                 - paidAt: 결제 완료 시간 (기본 내림차순)
     *                 - amount: 결제 금액
     *                 사용 예:
     *                 - 결제 시간 오름차순: ?sort=paidAt,asc
     *                 - 결제 금액 내림차순: ?sort=amount,desc
     *                 - 복수 정렬: ?sort=paidAt,desc&sort=amount,asc
     * @param condition 검색 조건. 다음 필드들을 포함합니다:
     *                  - customerId (Long): 고객 ID
     *                  - startDate (LocalDate): 검색 시작 날짜
     *                  - endDate (LocalDate): 검색 종료 날짜
     *                  - status (PaymentStatus): 결제 상태
     *                  - payType (String): 결제 유형
     *                  - minAmount (Long): 최소 결제 금액
     *                  - maxAmount (Long): 최대 결제 금액
     *                 모든 검색 조건은 선택적입니다. 제공되지 않은 조건은 검색 시 무시됩니다.
     * @return 검색된 결제 내역 페이지
     */
    @GetMapping("/search")
    @ResponseBody
    public GlobalResponse<Page<PaymentResponseDto.Get>> searchPayments(
            @AuthUser AuthUserInfo authUserInfo,
            Pageable pageable,
            PaymentSearchCondition condition
    ){
        Page<PaymentResponseDto.Get> responseDto = paymentService.searchPayments(authUserInfo,pageable,condition);
        return ApiUtils.success(ResponseMessage.PAYMENT_RETRIEVE_SUCCESS.getMessage(), responseDto);
    }


    /**
     * 사용자 보유 쿠폰 목록 조회
     * @return: 사용자 쿠폰 목록 조회 응답 DTO
     */
    @GetMapping("/user-coupons")
    public ResponseEntity<List<CouponUserResponseDto>> getUserCoupons(@RequestHeader("Authorization") String authToken) {
        List<CouponUserResponseDto> coupons = paymentService.getUserCoupons(authToken);
        return ResponseEntity.ok(coupons);
    }

    /**
     * 나에게 온 결제 요청 확인하기
     * @return: 서비스 제공자에게 온 결제 요청 목록
     */
    @GetMapping("/pending-requests")
    @ResponseBody
    public GlobalResponse<List<PendingPaymentRequest>> getPendingPaymentRequests(@AuthUser AuthUserInfo authUserInfo) {
        return ApiUtils.success(ResponseMessage.PAYMENT_RETRIEVE_SUCCESS.getMessage(), paymentService.getPendingPaymentRequestsByProviderId(authUserInfo));
    }

}
