package com.tk.gg.reservation.domain.repository;

import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.domain.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> searchReviews(ReviewSearchCondition searchCondition, Pageable pageable);
}
