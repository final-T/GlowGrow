package com.tk.gg.reservation.domain.service;


import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CustomerGradeDto;
import com.tk.gg.reservation.application.dto.ProviderGradeDto;
import com.tk.gg.reservation.domain.model.Grade;
import com.tk.gg.reservation.infrastructure.messaging.GradeForReservationEventDto;
import com.tk.gg.reservation.infrastructure.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class GradeDomainService {

    private final GradeRepository gradeRepository;

    public Page<CustomerGradeDto> getCustomerGradesByUserInfo(Long userId, Pageable pageable) {
        return gradeRepository.getCustomerGradesByUserInfo(userId, pageable);
    }

    public Page<ProviderGradeDto> getProviderGradesByUserInfo(Long userId, Pageable pageable) {
        return gradeRepository.getProviderGradesByUserInfo(userId, pageable);
    }

    public void createGradeForReservation(GradeForReservationEventDto gradeForReservationDto) {
        Grade customerGrade = Grade.builder().userId(gradeForReservationDto.customerId())
                .userType(UserRole.CUSTOMER)
                .reservationId(gradeForReservationDto.reservationId()).build();
        Grade providerGrade = Grade.builder().userId(gradeForReservationDto.providerId())
                .userType(UserRole.PROVIDER)
                .reservationId(gradeForReservationDto.reservationId()).build();
        gradeRepository.saveAll(List.of(customerGrade, providerGrade));
    }

    public Grade getGradeByUserId(Long userId){
        return gradeRepository.findByUserId(userId).orElseThrow(
                () -> new GlowGlowException(GlowGlowError.GRADE_NO_EXIST)
        );
    }


    //TODO : 리뷰에 평가 기능 추가 후 구현
    public void updateGradeForReview() {
    }
}
