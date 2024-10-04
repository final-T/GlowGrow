package com.tk.gg.reservation.application.service;


import com.tk.gg.reservation.application.dto.CreateReviewDto;
import com.tk.gg.reservation.application.dto.ReviewDto;
import com.tk.gg.reservation.application.dto.ReviewWithReservationDto;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.application.dto.UpdateReviewDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.ReviewDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

//TODO : 유저 Role 에 따른 데이터 검증 필요
//-> ex) 리뷰 남기는 사람이 제공자인지 사용자였는지 체크(양방향 리뷰이기 때문)
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewDomainService reviewDomainService;
    private final ReservationDomainService reservationDomainService;

    @Transactional
    public ReviewWithReservationDto createReview(CreateReviewDto dto) {
        Reservation reservation = reservationDomainService.getOne(dto.reservationId());
        return ReviewWithReservationDto.from(reviewDomainService.create(dto, reservation));
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
    public void updateReview(UUID reviewId, UpdateReviewDto dto) {
        reviewDomainService.update(reviewId, dto);
    }

    @Transactional
    public void deleteReview(UUID reviewId, String deletedBy) {
        reviewDomainService.delete(reviewId, deletedBy);
    }
}
