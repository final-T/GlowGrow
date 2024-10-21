package com.tk.gg.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.kafka.coupon.CouponUserResponseDto;
import com.tk.gg.common.kafka.payment.PaymentReservationResponseDto;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.client.CouponService;
import com.tk.gg.payment.application.client.ReservationService;
import com.tk.gg.payment.application.client.UserService;
import com.tk.gg.payment.application.dto.*;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.PendingPaymentRequest;
import com.tk.gg.payment.domain.service.PaymentDomainService;
import com.tk.gg.payment.domain.service.PendingPaymentRequestDomainService;
import com.tk.gg.payment.domain.type.PaymentStatus;
import com.tk.gg.payment.infrastructure.config.TossPaymentConfig;
import com.tk.gg.payment.infrastructure.messaging.NotificationPaymentKafkaProducer;
import com.tk.gg.payment.infrastructure.repository.PaymentRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final UserService userService;
    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final ReservationService reservationService;
    private final CouponService couponService;
    private final PendingPaymentRequestDomainService pendingPaymentRequestDomainService;
    private final NotificationPaymentKafkaProducer notificationPaymentKafkaProducer;


    @Transactional
    public Payment requestTossPayment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo) {
        boolean userExists = userService.isUserExistsByEmail(authUserInfo.getEmail());
        if (!userExists) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }

        Payment payment = paymentRepository.findById(requestDto.getPaymentId())
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_NO_EXIST));

        // 이미 결제된 정보면 결제 요청 X
        if(PaymentStatus.COMPLETED.equals(payment.getStatus())){
            throw new GlowGlowException(GlowGlowError.ALREADY_APPROVED);
        }

        payment.updateFromDto(requestDto); // 사용자가 [결제 하기] 시 변경 가능성 필드 :  쿠폰, 결제 금액 (쿠폰 적용 시)

        log.info("최종 결제 가격 : {} 할인 가격 : {} , 할인 후 가격 : {}  원래 가격 : {}" , payment.getAmount() , payment.getDiscountAmount() , payment.getDiscountedAmount(), payment.getOriginalAmount());
        return paymentRepository.save(payment);
    }

    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = findPaymentByOrderId(orderId); // orderId == PaymentId

        paymentDomainService.verifyPaymentAmount(payment, amount);  // 요청 가격과 결제된 금액 비교


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode params = objectMapper.createObjectNode();
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentSuccessDto result = null;
            // 최종 결제 승인 요청 URL : config에서 작성한 https://api.tosspayments.com/v1/payments/ + paymentKey
            result = restTemplate.postForObject(TossPaymentConfig.URL + paymentKey,
                    new HttpEntity<>(params,headers),
                    PaymentSuccessDto.class);

        paymentDomainService.completePayment(payment, paymentKey, result.getApprovedAt(), result.getMethod());

        // 결제 정보 저장
        paymentRepository.save(payment);

        // 쿠폰 사용을 했다면, 쿠폰 사용 Feign 호출
        if(payment.getCouponId() != null && payment.getStatus() == PaymentStatus.COMPLETED){
            couponService.useCoupon(payment.getCouponId(), payment.getCustomerId());
        }

        pendingPaymentRequestDomainService.softDeletePendingRequest(payment.getReservationId());

        // 결제 완료 알림
        String notificationMessage = String.format("결제가 성공적으로 완료되었습니다. 결제 금액: %d원", amount);
        notificationPaymentKafkaProducer.sendPaymentNotificationToUser(payment.getCustomerId(), notificationMessage ,true);

        return result;
    }

    @Transactional
    public PaymentFailDto tossPaymentFail(String code, String message, String orderId) {
        Payment payment = findPaymentByOrderId(orderId);

        if(PaymentStatus.COMPLETED.equals(payment.getStatus())){
            throw new GlowGlowException(GlowGlowError.ALREADY_APPROVED);
        }

        paymentDomainService.failPayment(payment, message);
        paymentRepository.save(payment);

        // 결제 완료 알림
        notificationPaymentKafkaProducer.sendPaymentNotificationToUser(payment.getCustomerId(), "결제 처리 중 오류가 발생했습니다." ,false);


        return PaymentFailDto.builder()
                .errorCode(code)
                .errorMessage(message)
                .orderId(orderId)
                .build();
    }


    public HttpHeaders getHeaders(){ // 요청 헤더에 꼭 Authorization 넣어줘야 함
        // 시크릿 키를 base64로 인코딩 한 값을 넣음
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder()
                        .encode((tossPaymentConfig.getSecretKey() + ":")
                                .getBytes(StandardCharsets.UTF_8)));

        // Basic Authorization 인가 코드를 보낼 때 시크릿 키를 Base64를 사용하여 인코딩하여 보내게 되는데,
        // 이 때, {시크릿 키 + ":"} 조합으로 인코딩해야한다.
        //headers.set("Authorization", "Basic " + encodedAuthKey);
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }


    // Payment 존재 여부 확인
    private Payment findPaymentByOrderId(String orderId) {
        return paymentRepository.findByPaymentId(UUID.fromString(orderId))
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_NO_EXIST));
    }

    // 결제 키 + 사용자 ID를 사용하여 결제 정보 존재 여부 확인
    @Transactional(readOnly = true)
    public Payment findPaymentByPaymentKey(String paymentKey, Long userId) {
        return paymentRepository.findByPaymentKey(paymentKey, userId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_NO_EXIST));
    }

    @Transactional
    public void tossPaymentUserCancel(String code, String message, AuthUserInfo authUserInfo) {
        Payment payment = paymentRepository.findLatestByUserId(authUserInfo.getId())
                        .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_NO_EXIST));

        paymentDomainService.cancelPayment(payment, message);
        paymentRepository.save(payment);
    }

    // 결제 준비 메서드 (provider용)
    @Transactional
    public UUID preparePayment(PaymentRequestDto requestDto, AuthUserInfo authUserInfo) {

        // 결제 요청 준비를 하는 사용자(authUserInfo.getId)가 예약의 제공자 Id가 아니라면 예외 발생(reservationService)
        PaymentReservationResponseDto reservationInfo = reservationService.getOneReservation(requestDto.getReservationId(), authUserInfo.getToken());
        Long customerId = reservationInfo.customerId(); //예약의 이용자 ID
        Payment payment = paymentDomainService.createPayment(requestDto, authUserInfo, customerId);

        log.info("@@@@@ customerId : {} & providerId :{} " , reservationInfo.customerId(), reservationInfo.serviceProviderId());

        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment.getPaymentId();
    }

    public List<CouponUserResponseDto> getUserCoupons(String authToken) {
        List<CouponUserResponseDto> allCoupons = couponService.getUserCoupons(authToken);

        // 쿠폰이 활성화(ACTIVE)된 쿠폰만 가져옴
        List<CouponUserResponseDto> activeCoupons = allCoupons.stream()
                .filter(coupon -> "ACTIVE".equals(coupon.getStatus()))
                .toList();

        for (CouponUserResponseDto coupon : activeCoupons) {
            log.info("Coupon: {}", coupon);
            log.debug("Coupon Details - ID: {}, Promotion ID: {}, Description: {}, Discount Type: {}, " +
                            "Discount Value: {}, Max Discount: {}, Valid From: {}, Valid Until: {}, Status: {}, Is Used: {}",
                    coupon.getCouponId(), coupon.getPromotionId(), coupon.getDescription(),
                    coupon.getDiscountType(), coupon.getDiscountValue(), coupon.getMaxDiscount(),
                    coupon.getValidFrom(), coupon.getValidUntil(), coupon.getStatus(), coupon.getIsUsed());
        }
        return activeCoupons;
    }

    @Transactional(readOnly = true)
    public PaymentRequestDto getPreparedPayment(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.PAYMENT_NO_EXIST));

        return PaymentRequestDto.builder()
                .userId(payment.getUserId())
                .customerId(payment.getCustomerId())
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .orderName(payment.getOrderName())
                .reservationId(payment.getReservationId())
                .status(payment.getStatus())
                .build();
    }

    public String getTossClientKey() {
        return tossPaymentConfig.getClientKey();
    }

    // 내 결제 조회
    @Transactional(readOnly = true)
    public List<PaymentResponseDto.Get> getPayments(AuthUserInfo authUserInfo) {
        // 사용자 존재 여부 검증
        boolean userExists = userService.isUserExistsByEmail(authUserInfo.getEmail());
        if (!userExists) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }

        List<Payment> paymentList = paymentDomainService.getPaymentsByCustomerId(authUserInfo.getId());

        if(paymentList.isEmpty()){
            throw new GlowGlowException(GlowGlowError.MY_PAYMENT_NOT_FOUND);
        }

        return paymentList.stream()
                .map(PaymentResponseDto.Get::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponseDto.Get> searchPayments(AuthUserInfo authUserInfo, Pageable pageable, PaymentSearchCondition condition) {
        // 사용자 존재 여부 검증
        boolean userExists = userService.isUserExistsByEmail(authUserInfo.getEmail());
        if (!userExists) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }

        // 사용자 역할에 따른 접근 제어
        switch (authUserInfo.getUserRole()) {
            case MASTER:
                // MASTER는 모든 결제 내역을 볼 수 있음
                break;
            case PROVIDER:
            case CUSTOMER:
                // PROVIDER와 CUSTOMER는 자신의 결제 내역만 볼 수 있음
                condition.setCustomerId(authUserInfo.getId());
                break;
            default:
                throw new GlowGlowException(GlowGlowError.AUTH_UNAUTHORIZED);
        }

        return paymentDomainService.searchPayments(condition, pageable);
    }

    /**
     * 결제 요청 이벤트 수신했을 때 결제요청 데이터 저장 메서드
     * @return:
     */
    @Transactional
    public void savePendingPaymentRequest(PaymentReservationResponseDto event) {
        PendingPaymentRequest request = pendingPaymentRequestDomainService.createPendingPaymentRequest(
                event.reservationId(), event.customerId(), event.serviceProviderId(), event.price());
        log.info("Pending payment request saved: {}", request);
    }


    @Transactional(readOnly = true)
    public List<PendingPaymentRequest> getPendingPaymentRequestsByProviderId(AuthUserInfo authUserInfo) {
        UserRole userRole = authUserInfo.getUserRole();

        if(!UserRole.MASTER.equals(userRole)){
            throw new GlowGlowException(GlowGlowError.PAYMENT_NO_AUTH_PERMISSION_DENIED);
        }

        Long providerId = authUserInfo.getId();

        List<PendingPaymentRequest> requests = pendingPaymentRequestDomainService.getPendingRequestsForProvider(providerId);
        if (requests.isEmpty()) {
            log.info("No pending payment requests found for provider: {}", providerId);
            throw new GlowGlowException(GlowGlowError.PAYMENT_REQUEST_NOT_FOUND);
        }
        return requests;
    }
}
