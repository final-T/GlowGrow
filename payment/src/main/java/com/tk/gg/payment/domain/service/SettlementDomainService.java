package com.tk.gg.payment.domain.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.SettlementDetailDto;
import com.tk.gg.payment.application.dto.SettlementDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.model.SettlementDetail;
import com.tk.gg.payment.domain.type.CouponType;
import com.tk.gg.payment.domain.type.SettlementStatus;
import com.tk.gg.payment.infrastructure.repository.SettlementRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SETTLEMENT-DOMAIN-SERVICE")
public class SettlementDomainService {

    private final SettlementRepository settlementRepository;
    private final SettlementDetailDomainService settlementDetailDomainService;
    private final PaymentDomainService paymentDomainService;

    private static final Long SYSTEM_USER_ID = 999L; // 시스템 사용자 ID
    private static final String SYSTEM_USER_NAME = "AUTOMATED_SYSTEM";

    public Settlement createSettlement(Long providerId, List<Payment> payments, Long settlementTime, AuthUserInfo authUserInfo) {

        long totalAmount = calculateTotalAmount(payments);

        Settlement settlement = Settlement.createSettlementBuilder()
                .providerId(providerId)
                .totalAmount(totalAmount)
                .settlementTime(settlementTime)
                .authUserInfo(authUserInfo)
                .build();

        settlement = settlementRepository.save(settlement);

        // 정산 세부사항 저장
        settlementDetailDomainService.createSettlementDetails(settlement,payments);

        // 결제들을 정산 완료 상태로 표시
        paymentDomainService.markPaymentsAsSettled(payments);

        return settlement;
    }

    public Settlement updateSettlement(UUID settlementId, SettlementDto.UpdateRequest requestDto, AuthUserInfo authUserInfo) {
        Settlement settlement = getSettlementById(settlementId);

        if (requestDto.getStatus() != null) {
            updateSettlementStatus(settlement, requestDto.getStatus(), authUserInfo);
        }
        if (requestDto.getTotalAmount() != null) {
            updateSettlementAmount(settlement, requestDto.getTotalAmount(), authUserInfo);
        }

        return settlementRepository.save(settlement);
    }

    public Page<SettlementDto.Response> searchSettlements(Pageable pageable, SettlementDto.SearchRequest requestDto) {
        Page<SettlementDto.Response> result =  settlementRepository.findSettlementsByCondition(requestDto, pageable);
        if(result.isEmpty()){
            throw new GlowGlowException(GlowGlowError.NO_SEARCH_RESULTS);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<SettlementDetailDto.Response> getSettlementDetailsByProviderIdAndTime(Long providerId, Long settlementTime) {
        // SettlementDetail 리스트를 가져옵니다.
        List<SettlementDetail> details = settlementDetailDomainService.getSettlementDetailsByProviderAndTime(providerId, settlementTime);
        return details.stream()
                .map(SettlementDetailDto.Response::from)
                .collect(Collectors.toList());
    }



    public Settlement getSettlementById(UUID settlementId) {
        return settlementRepository.findBySettlementId(settlementId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.SETTLEMENT_NO_EXIST));
    }

    public void updateSettlementStatus(Settlement settlement, SettlementStatus newStatus, AuthUserInfo authUserInfo) {
        settlement.updateStatus(newStatus, authUserInfo);
    }

    public void updateSettlementAmount(Settlement settlement, Long newAmount, AuthUserInfo authUserInfo) {
        settlement.updateTotalAmount(newAmount, authUserInfo);
    }

    public void deleteSettlement(UUID settlementId, AuthUserInfo authUserInfo) {
        Settlement settlement = getSettlementById(settlementId);
        settlement.softDelete(authUserInfo);
    }

    /**
     * 주어진 결제 목록에 대한 총 정산 금액을 계산합니다.
     * 각 결제에 대해 calculateSettlementAmountForPayment 메서드를 호출하여
     * 개별 정산 금액을 계산한 후, 이를 모두 합산합니다.
     *
     * @param payments 정산할 결제 목록
     * @return 총 정산 금액
     */
    private long calculateTotalAmount(List<Payment> payments) {
        return payments.stream()
                .mapToLong(this::calculateSettlementAmountForPayment)
                .sum();
    }

    /**
     * 개별 결제에 대한 정산 금액을 계산합니다.
     * 쿠폰 사용 여부와 쿠폰 타입에 따라 다른 계산 방식을 적용합니다:
     * 1. 쿠폰을 사용하지 않은 경우: 결제 금액 그대로 사용
     * 2. PROVIDER 쿠폰을 사용한 경우: 결제 금액 사용
     * 3. GLOWGROW 쿠폰을 사용한 경우: 원래 금액 사용
     *
     * @param payment 정산할 결제 정보
     * @return 계산된 정산 금액
     * @throws IllegalStateException 알 수 없는 쿠폰 타입인 경우 발생
     */
    private long calculateSettlementAmountForPayment(Payment payment) {
        // 쿠폰을 사용하지 않은 경우 또는 PROVIDER 쿠폰을 사용한 경우
        if (payment.getCouponId() == null || payment.getCouponType() == CouponType.PROVIDER) {
            return payment.getAmount();
        }

        // GLOWGROW 쿠폰을 사용한 경우
        if (payment.getCouponType() == CouponType.GLOWGROW) {
            return payment.getOriginalAmount();
        }
        // 알 수 없는 쿠폰 타입
        throw new IllegalStateException("Unknown coupon type: " + payment.getCouponType());
    }

    // 스케줄링용 정산
    @Transactional
    public Settlement createAutomatedSettlement(Long providerId, List<Payment> payments, Long settlementTime) {
        long totalAmount = calculateTotalAmount(payments);

        AuthUserInfo systemUser = new AuthUserInfo() {
            @Override
            public String getToken() {
                return "";
            }

            @Override
            public Long getId() {
                return SYSTEM_USER_ID;
            }

            @Override
            public String getUsername() {
                return SYSTEM_USER_NAME;
            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public UserRole getUserRole() {
                return null;
            }
        };


        Settlement settlement = Settlement.createSettlementBuilder()
                .providerId(providerId)
                .totalAmount(totalAmount)
                .settlementTime(settlementTime)
                .isAutomated(true) // 자동화된 정산임을 표시
                .authUserInfo(systemUser)
                .build();

        settlement = settlementRepository.save(settlement);

        // 정산 세부사항 저장
        settlementDetailDomainService.createSettlementDetails(settlement, payments);

        // 결제들을 정산 완료 상태로 표시
        paymentDomainService.markPaymentsAsSettled(payments);

        return settlement;
    }

}
