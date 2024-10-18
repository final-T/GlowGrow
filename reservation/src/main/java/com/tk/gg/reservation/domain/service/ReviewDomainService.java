package com.tk.gg.reservation.domain.service;


import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CreateReviewDto;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.application.dto.UpdateReviewDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.infrastructure.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.REVIEW_NO_EXIST;

@RequiredArgsConstructor
@Service
public class ReviewDomainService {
    private final ReviewRepository reviewRepository;

    public Review create(CreateReviewDto dto, Reservation reservation) {
        return reviewRepository.save(dto.toEntity(reservation));
    }

    public Page<Review> searchReviews(ReviewSearchCondition searchCondition, Pageable pageable) {
        return reviewRepository.searchReviews(searchCondition, pageable);
    }

    public Review getOne(UUID reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new GlowGlowException(REVIEW_NO_EXIST)
        );
    }

    public void update(Review review, UpdateReviewDto dto) {
        review.update(dto);
    }

    public void delete(Review review, String deletedBy) {
        review.delete(deletedBy);
    }

    public boolean existsByReservationId(UUID reservationId) {
        return reviewRepository.existsByReservationId(reservationId);
    }

    public List<Review> getReviewsByReservationId(UUID reservationId) {
        return reviewRepository.findAllByReservationId(reservationId);
    }
}
