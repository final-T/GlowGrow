package com.tk.gg.reservation.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CreateReviewDto;
import com.tk.gg.reservation.application.dto.GradeDto;
import com.tk.gg.reservation.application.dto.ReviewWithReservationDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.infrastructure.messaging.GradeKafkaProducer;
import com.tk.gg.security.user.AuthUserInfoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewDomainService reviewDomainService;

    @Mock
    private ReservationDomainService reservationDomainService;

    @Mock
    private GradeKafkaProducer kafkaProducer;

    private CreateReviewDto validCreateReviewDto;
    private Reservation validReservation;
    private Review review;
    private TimeSlot timeSlot;

    @BeforeEach
    void setUp() {
        // 예약자가 리뷰 남기는 것만 setting
        timeSlot = TimeSlot.builder().id(UUID.randomUUID())
                .serviceProviderId(2L)
                .availableDate(LocalDate.parse("2024-10-04", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .availableTime(20)
                .build();
        validReservation = Reservation.builder()
                .id(UUID.randomUUID())
                .timeSlot(timeSlot)
                .customerId(1L)
                .serviceProviderId(2L)
                .reservationDate(LocalDate.parse("2024-10-04", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .reservationTime(20)
                .reservationStatus(ReservationStatus.CHECK)
                .price(0)
                .build();

        validCreateReviewDto = CreateReviewDto.builder().reservationId(validReservation.getId())
                .reviewerId(1L).targetUserId(2L).rating(5).build();

        review = Review.builder().id(UUID.randomUUID()).reservation(validReservation)
                .reviewerId(1L).targetUserId(2L).rating(5) .build();
    }

    @Test
    @DisplayName("리뷰 생성 성공 - 예약 상태가 DONE일 때")
    void createReviewSuccessWhenReservationStatusIsDone() {
        // Given
        validReservation.updateStatus(ReservationStatus.DONE);
        when(reservationDomainService.getOne(any(UUID.class))).thenReturn(validReservation);
        when(reviewDomainService.create(any(CreateReviewDto.class), any(Reservation.class)))
                .thenReturn(review);
        willDoNothing().given(kafkaProducer).sendReviewEventForGrade(any(GradeDto.class));

        // When
        ReviewWithReservationDto result = reviewService.createReview(validCreateReviewDto,
                AuthUserInfoImpl.builder().id(1L).email("testuser@email.com").token("test-token")
                        .username("testuser").userRole(UserRole.CUSTOMER).build()
        );

        // Then
        then(kafkaProducer).should().sendReviewEventForGrade(any(GradeDto.class));
        verify(reservationDomainService, times(1)).getOne(any(UUID.class));
        verify(reviewDomainService, times(1)).
                create(any(CreateReviewDto.class), any(Reservation.class));
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CHECK", "REFUSED", "CANCEL"})
    @DisplayName("리뷰 생성 실패 - 예약 상태가 CHECK, REFUSED, CANCEL일 때")
    void createReviewFailWhenInvalidReservationStatus(ReservationStatus invalidStatus) {
        // Given
        validReservation.updateStatus(invalidStatus);
        when(reservationDomainService.getOne(any())).thenReturn(validReservation);

        // Then
        assertThatThrownBy(() -> reviewService.createReview(validCreateReviewDto,
                AuthUserInfoImpl.builder().id(1L).email("testuser@email.com").token("test-token")
                        .username("testuser").userRole(UserRole.CUSTOMER).build()
                )
        )
                .isInstanceOf(GlowGlowException.class)
                .hasMessage(GlowGlowError.REVIEW_CREATE_FAILED.getMessage());
    }

}