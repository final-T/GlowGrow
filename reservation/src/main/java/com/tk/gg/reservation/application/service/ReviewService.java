package com.tk.gg.reservation.application.service;


import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.client.UserServiceImpl;
import com.tk.gg.reservation.application.dto.*;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.infrastructure.messaging.GradeKafkaProducer;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewDomainService reviewDomainService;
    private final ReservationDomainService reservationDomainService;
    private final UserServiceImpl userClient;
    private final GradeKafkaProducer gradeKafkaProducer;

    //TODO : 예약과 관련된 사람만 해당 예약에 대한 리뷰 작성 가능하게 검증
    @Transactional
    public ReviewWithReservationDto createReview(CreateReviewDto dto, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(dto.reservationId());
        // 예약 상태 검증 -> 예약 체크, 거절, 취소인 경우에는 리뷰 불가
        if (reservation.getReservationStatus().equals(ReservationStatus.CHECK) ||
                reservation.getReservationStatus().equals(ReservationStatus.REFUSED) ||
                reservation.getReservationStatus().equals(ReservationStatus.CANCEL)
        ) {
            throw new GlowGlowException(REVIEW_CREATE_FAILED);
        }
        if (!dto.reviewerId().equals(userInfo.getId())){
            throw new GlowGlowException(REVIEW_WRONG_REVIEWER_ID);
        }
        //리뷰 생성 및 저장
        Review review = reviewDomainService.create(dto, reservation);
        // GradeDto 로 변환 후 review 에 대한 평가정보 반영
        gradeKafkaProducer.sendReviewEventForGrade(dto.toGradeDto(userInfo.getUserRole(), review.getId()));
        return ReviewWithReservationDto.from(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> searchReviews(ReviewSearchCondition searchCondition, Pageable pageable) {
        return reviewDomainService.searchReviews(searchCondition, pageable).map(ReviewDto::from);
    }

    @Transactional(readOnly = true)
    public ReviewWithReservationDto getOneReview(UUID reviewId) {
        return ReviewWithReservationDto.from(reviewDomainService.getOne(reviewId));
    }

    @Transactional
    public void updateReview(UUID reviewId, UpdateReviewDto dto, AuthUserInfo userInfo) {
        Review review = checkIsUserExistAndReviewOwner(reviewId, userInfo);
        reviewDomainService.update(review, dto);
    }

    @Transactional
    public void deleteReview(UUID reviewId, AuthUserInfo userInfo) {
        Review review = checkIsUserExistAndReviewOwner(reviewId, userInfo);
        reviewDomainService.delete(review, userInfo.getEmail());
    }

    private Review checkIsUserExistAndReviewOwner(UUID reviewId, AuthUserInfo userInfo){
        Review review = reviewDomainService.getOne(reviewId);
        if (!userClient.isUserExistsByEmail(userInfo.getEmail()))
            throw new GlowGlowException(AUTH_INVALID_CREDENTIALS);
        if (!review.getReviewerId().equals(userInfo.getId()))
            throw new GlowGlowException(REVIEW_NOT_OWNER);
        return review;
    }
}
