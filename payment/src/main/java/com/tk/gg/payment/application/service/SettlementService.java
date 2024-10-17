package com.tk.gg.payment.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.payment.application.dto.SettlementDetailDto;
import com.tk.gg.payment.application.dto.SettlementDto;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.model.Settlement;
import com.tk.gg.payment.domain.model.SettlementDetail;
import com.tk.gg.payment.domain.service.PaymentDomainService;
import com.tk.gg.payment.domain.service.SettlementDetailDomainService;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SETTLEMENT-APPLICATION_SERVICE")
public class SettlementService {

    private final PaymentDomainService paymentDomainService;
    private final SettlementDomainService settlementDomainService;
    private final SettlementDetailDomainService settlementDetailDomainService;


    @Transactional
    public SettlementDto.Response createSettlement(SettlementDto.Request requestDto, AuthUserInfo authUserInfo) {
        // MASTER만 접근 가능
        if(!authUserInfo.getUserRole().equals(UserRole.MASTER)){
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_AUTH_PERMISSION_DENIED);
        }
        Long settlementTime = convertToYearMonth(requestDto.getEndDate());

        LocalDateTime startDate = convertLongToLocalDateTime(requestDto.getStartDate());
        LocalDateTime endDate = convertLongToLocalDateTime(requestDto.getEndDate()).plusDays(1).minusSeconds(1);

        log.info("Searching for payments between {} and {}", startDate, endDate);


        List<Payment> payments = paymentDomainService.findPaymentsByProviderIdAndDateRange(
                requestDto.getProviderId(), startDate, endDate
        );

        // 해당 기간에 대한 결제 정보가 없을 경우
        if (payments.isEmpty()) {
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_PAYMENTS_FOUND);
        }

        Settlement savedSettlement = settlementDomainService.createSettlement(
                requestDto.getProviderId(), payments, settlementTime, authUserInfo);

        return SettlementDto.Response.from(savedSettlement);
    }

    @Transactional(readOnly = true)
    public SettlementDto.Response getSettlementById(UUID settlementId, AuthUserInfo authUserInfo) {
        // MASTER만 접근 가능
        if(!authUserInfo.getUserRole().equals(UserRole.MASTER)){
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_AUTH_PERMISSION_DENIED);
        }
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
    public Page<SettlementDto.Response> searchSettlements(Pageable pageable, SettlementDto.SearchRequest requestDto, AuthUserInfo authUserInfo) {
        UserRole userRole = authUserInfo.getUserRole();

        // 일반적으로 검색은 권한체크를 하지 않지만 정산의 경우 admin로직이기 때문에 권한체크 필요
        // MASTER : 모든 권한, CUSTOMER : 권한 X, PROVIDER : 자신의 정산만 조회 가능
        if (UserRole.CUSTOMER.equals(userRole)) {
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_AUTH_PERMISSION_DENIED);
        } else if (UserRole.PROVIDER.equals(userRole)) {
            if(requestDto.getProviderId() == null){
                throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_PROVIDER_ID_FOR_PROVIDER);
            }
            if(!requestDto.getProviderId().equals(authUserInfo.getId())){
                throw new GlowGlowException(GlowGlowError.SETTLEMENT_SELF_ACCESS_ONLY);
            }
        }
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
        LocalDateTime endDate = LocalDateTime.now().withDayOfMonth(1).minusSeconds(1); // 지난 달의 마지막 날
        LocalDateTime startDate = endDate.minusMonths(1).withDayOfMonth(1); // 지난 달의 첫 날

        //TODO : 빈값이 반환됨
        List<Long> allProviderIds = paymentDomainService.getAllProviderIds();

        if (allProviderIds.isEmpty()) {
            log.info("@@@@@ No providers found with status 'COMPLETED'.");
        }

        for (Long providerId : allProviderIds) {
            log.info("@"+providerId);
        }

    }

    @Transactional(readOnly = true)
    public List<SettlementDetailDto.Response> getSettlementDetailsByProviderAndTime(AuthUserInfo authUserInfo, Long settlementTime) {
        // 권한 검증
        UserRole userRole = authUserInfo.getUserRole();

        if (UserRole.CUSTOMER.equals(userRole)) {
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_AUTH_PERMISSION_DENIED);
        }


        // 로그인한 사용자(provider)의 id + 정산 일자로  SettlementDetail 목록을 가져옴
        List<SettlementDetail> details = settlementDetailDomainService.getSettlementDetailsByProviderAndTime(authUserInfo.getId(), settlementTime);

        if(details.isEmpty()){
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_EXIST);
        }

        // 리스트 형태로 정산에 포함된 각 결제의 세부 내역과 관련 결제 정보가 포함
        return details.stream()
                .map(SettlementDetailDto.Response::from)
                .collect(Collectors.toList());
    }


    // 권한 검사 메서드
    private void checkPermission(UserRole userRole) {
        if (UserRole.CUSTOMER.equals(userRole) || UserRole.PROVIDER.equals(userRole)) {
            throw new GlowGlowException(GlowGlowError.SETTLEMENT_NO_AUTH_PERMISSION_DENIED);
        }
    }


    private LocalDateTime convertLongToLocalDateTime(Long date) {
        int year = (int) (date / 10000);
        int month = (int) ((date % 10000) / 100);
        int day = (int) (date % 100);
        return LocalDateTime.of(year, month, day, 0, 0);
    }

    private Long convertToYearMonth(Long date) {
        return date / 100;
    }


}
