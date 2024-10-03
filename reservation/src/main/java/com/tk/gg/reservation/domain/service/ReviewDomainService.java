package com.tk.gg.reservation.domain.service;


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

import java.util.UUID;

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
                () -> new IllegalArgumentException("ID 에 해당하는 리뷰 정보가 없습니다.")
        );
    }

    public void update(UUID reviewId, UpdateReviewDto dto) {
        Review review = getOne(reviewId);
        review.update(dto);
    }

    public void delete(UUID reviewId, String deletedBy) {
        Review review = getOne(reviewId);
        review.delete(deletedBy);
    }
}
