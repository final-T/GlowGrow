package com.tk.gg.reservation.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.GradeDto;
import com.tk.gg.reservation.application.dto.ResultGradeDto;
import com.tk.gg.reservation.domain.service.GradeDomainService;
import com.tk.gg.reservation.infrastructure.messaging.GradeForReservationEventDto;
import com.tk.gg.reservation.infrastructure.messaging.GradeForReviewEventDto;
import com.tk.gg.reservation.infrastructure.messaging.ToUserKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "GRADE-UP-CONSUMER")
@RequiredArgsConstructor
@Service
public class GradeService {

    private final GradeDomainService gradeDomainService;
//    private final ReviewDomainService reviewDomainService;
    private final ToUserKafkaProducer userKafkaProducer;

    // 예약 완료 -> 평가정보 반영 리스너 -> 이벤트 발행
    @KafkaListener(topics = "grade-reservation", groupId = "grade-group")
    public void handleGradeForReservation(GradeForReservationEventDto event) {
        log.info("예약에 대한 평가반영 이벤트 수신 및 저장: {}", event);
        //TODO : 예약 검증?
        gradeDomainService.createGradeForReservation(event);
        log.info("예약 -> 평가정보 저장 완료");
        userKafkaProducer.sendGradeForReservationEventToUser(event);
        log.info("유저에게 평가정보 업데이트 송신 완료");
    }

    // 리뷰 및 평점 작성 -> 평가정보 반영 리스너 -> 이벤트 발행
    @KafkaListener(topics = "grade-review", groupId = "grade-group")
    public void handleGradeForReview(GradeForReviewEventDto event) {
        log.info("예약에 대한 평가반영 이벤트 수신: {}", event);
        //TODO :  review 를 받을 때, 평가정보에 대한 내용도 받게 함
//        gradeDomainService.updateGradeForReview();
        log.info("예약 -> 평가정보 저장 완료");
        userKafkaProducer.sendGradeForReviewEventToUser(event);
        log.info("유저에게 평가정보 업데이트 송신 완료");
    }

    public Page<GradeDto> getGradesByUserInfo(Long userId, UserRole userType, Pageable pageable) {
        if (userType == UserRole.CUSTOMER) {
            return gradeDomainService.getCustomerGradesByUserInfo(userId, pageable).map(GradeDto::from);
        } else if (userType == UserRole.PROVIDER) {
            return gradeDomainService.getProviderGradesByUserInfo(userId, pageable).map(GradeDto::from);
        } else {
            throw new GlowGlowException(GlowGlowError.GRADE_INVALID_ROLES);
        }
    }

    public GradeDto getGradeForUserAndReservation(Long userId, UUID reservationId) {
        return GradeDto.from(gradeDomainService.getGradeForUserAndReservation(userId, reservationId));
    }
    public GradeDto getGradeForUserAndReview(Long userId, UUID reviewId) {
        return GradeDto.from(gradeDomainService.getGradeForUserAndReview(userId, reviewId));
    }


    public ResultGradeDto getUserGradeSummary(Long userId, UserRole userType, Pageable pageable) {
        List<GradeDto> grades = getGradesByUserInfo(userId, userType, pageable).getContent();

        // 합계 변수 초기화
        int totalProviderServiceQuality = 0;
        int totalProviderProfessionalism = 0;
        int totalProviderCommunication = 0;
        int totalProviderPunctuality = 0;
        int totalProviderPriceSatisfaction = 0;
        int totalCustomerCommunication = 0;
        int totalCustomerPunctuality = 0;
        int totalCustomerManners = 0;
        int totalCustomerPaymentPromptness = 0;

        // 평가 수
        int count = grades.size();

        if (userType.equals(UserRole.PROVIDER)) {
            for (GradeDto grade : grades) {
                totalProviderServiceQuality += grade.providerServiceQuality();
                totalProviderProfessionalism += grade.providerProfessionalism();
                totalProviderCommunication += grade.providerCommunication();
                totalProviderPunctuality += grade.providerPunctuality();
                totalProviderPriceSatisfaction += grade.providerPriceSatisfaction();
            }
        } else if (userType.equals(UserRole.CUSTOMER)) {
            for (GradeDto grade : grades) {
                totalCustomerCommunication += grade.customerCommunication();
                totalCustomerPunctuality += grade.customerPunctuality();
                totalCustomerManners += grade.customerManners();
                totalCustomerPaymentPromptness += grade.customerPaymentPromptness();
            }
        }

        // 평균 계산 (평가 항목들이 없을 수 있으니 0으로 나누는 상황을 방지)
        int averageProviderServiceQuality = totalProviderServiceQuality > 0 ? totalProviderServiceQuality / count : 0;
        int averageProviderProfessionalism = totalProviderProfessionalism > 0 ? totalProviderProfessionalism / count : 0;
        int averageProviderCommunication = totalProviderCommunication > 0 ? totalProviderCommunication / count : 0;
        int averageProviderPunctuality = totalProviderPunctuality > 0 ? totalProviderPunctuality / count : 0;
        int averageProviderPriceSatisfaction = totalProviderPriceSatisfaction > 0 ? totalProviderPriceSatisfaction / count : 0;

        int averageCustomerCommunication = totalCustomerCommunication > 0 ? totalCustomerCommunication / count : 0;
        int averageCustomerPunctuality = totalCustomerPunctuality > 0 ? totalCustomerPunctuality / count : 0;
        int averageCustomerManners = totalCustomerManners > 0 ? totalCustomerManners / count : 0;
        int averageCustomerPaymentPromptness = totalCustomerPaymentPromptness > 0 ? totalCustomerPaymentPromptness / count : 0;

        // 결과 DTO 생성 및 반환
        return ResultGradeDto.builder()
                .totalCount(count)
                .totalProviderServiceQuality(totalProviderServiceQuality)
                .totalProviderProfessionalism(totalProviderProfessionalism)
                .totalProviderCommunication(totalProviderCommunication)
                .totalProviderPunctuality(totalProviderPunctuality)
                .totalProviderPriceSatisfaction(totalProviderPriceSatisfaction)
                .totalCustomerCommunication(totalCustomerCommunication)
                .totalCustomerPunctuality(totalCustomerPunctuality)
                .totalCustomerManners(totalCustomerManners)
                .totalCustomerPaymentPromptness(totalCustomerPaymentPromptness)
                .averageProviderServiceQuality(averageProviderServiceQuality)
                .averageProviderProfessionalism(averageProviderProfessionalism)
                .averageProviderCommunication(averageProviderCommunication)
                .averageProviderPunctuality(averageProviderPunctuality)
                .averageProviderPriceSatisfaction(averageProviderPriceSatisfaction)
                .averageCustomerCommunication(averageCustomerCommunication)
                .averageCustomerPunctuality(averageCustomerPunctuality)
                .averageCustomerManners(averageCustomerManners)
                .averageCustomerPaymentPromptness(averageCustomerPaymentPromptness)
                .build();

    }
}