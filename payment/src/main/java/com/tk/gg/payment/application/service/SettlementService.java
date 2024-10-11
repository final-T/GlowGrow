package com.tk.gg.payment.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.SettlementDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.service.PaymentDomainService;
import com.tk.gg.payment.domain.service.SettlementDomainService;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SETTLEMENT-APPLICATION_SERVICE")
public class SettlementService {

    private final PaymentDomainService paymentDomainService;
    private final SettlementDomainService settlementDomainService;


    @Transactional
    public SettlementDto.Response createSettlement(SettlementDto.Request requestDto, AuthUserInfo authUserInfo) {
        List<Payment> payments = paymentDomainService.findPaymentsByProviderIdAndDateRange(
                requestDto.getProviderId(), requestDto.getStartDate(), requestDto.getEndDate()
        );

        Settlement savedSettlement = settlementDomainService.createSettlement(
                requestDto.getProviderId(), payments, authUserInfo);

        return SettlementDto.Response.from(savedSettlement);
    }

    @Transactional(readOnly = true)
    public SettlementDto.Response getSettlementById(UUID settlementId) {
        Settlement settlement = settlementDomainService.getSettlementById(settlementId);
        return SettlementDto.Response.from(settlement);
    }


    @Transactional
    public SettlementDto.Response updateSettlementById(UUID settlementId, SettlementDto.UpdateRequest requestDto, AuthUserInfo authUserInfo) {
        // 권한 확인
        checkPermission(authUserInfo.getUserRole());

        Settlement updatedSettlement = settlementDomainService.updateSettlement(settlementId, requestDto, authUserInfo);

        return SettlementDto.Response.from(updatedSettlement);
    }

    @Transactional(readOnly = true)
    public Page<SettlementDto.Response> searchSettlements(Pageable pageable, SettlementDto.SearchRequest requestDto) {
        return settlementDomainService.searchSettlements(pageable,requestDto);
    }

    @Transactional
    public void deleteSettlement(UUID settlementId, AuthUserInfo authUserInfo) {
        // 사용자 권한 확인
        checkPermission(authUserInfo.getUserRole());
        settlementDomainService.deleteSettlement(settlementId, authUserInfo);
    }

    @Transactional
    public void processAutomaticSettlements() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1); // 어제 일자 구하기

    }

    // 권한 검사 메서드
    private void checkPermission(UserRole userRole) {
        if (UserRole.CUSTOMER.equals(userRole) || UserRole.PROVIDER.equals(userRole)) {
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_AUTH_PERMISSION_DENIED);
        }
    }


}
