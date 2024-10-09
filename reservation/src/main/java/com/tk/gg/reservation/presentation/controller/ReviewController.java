package com.tk.gg.reservation.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.application.service.ReviewService;
import com.tk.gg.reservation.presentation.request.CreateReviewRequest;
import com.tk.gg.reservation.presentation.request.UpdateReviewRequest;
import com.tk.gg.reservation.presentation.response.ReviewResponse;
import com.tk.gg.reservation.presentation.response.ReviewWithReservationResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public GlobalResponse<ReviewWithReservationResponse> createReview(
            @RequestBody @Valid CreateReviewRequest request,
            @AuthUser AuthUserInfo userInfo
            ){
        return ApiUtils.success(REVIEW_CREATE_SUCCESS.getMessage(),
                ReviewWithReservationResponse.from(reviewService.createReview(request.toDto(), userInfo))
        );
    }

    @GetMapping
    public GlobalResponse<Page<ReviewResponse>> getAllReviews(
            ReviewSearchCondition searchCondition,
            @PageableDefault(sort = {"createdAt, updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiUtils.success(REVIEW_RETRIEVE_SUCCESS.getMessage(),
                reviewService.searchReviews(searchCondition, pageable).map(ReviewResponse::from)
        );
    }

    @GetMapping("/{reviewId}")
    public GlobalResponse<ReviewWithReservationResponse> getOneReview(
            @PathVariable(value = "reviewId") UUID reviewId
    ){
        return ApiUtils.success(REVIEW_RETRIEVE_SUCCESS.getMessage(),
                ReviewWithReservationResponse.from(reviewService.getOneReview(reviewId))
        );
    }

    @PutMapping("/{reviewId}")
    public GlobalResponse<String> updateReview(
            @PathVariable(value = "reviewId") UUID reviewId,
            @RequestBody @Valid UpdateReviewRequest request,
            @AuthUser AuthUserInfo userInfo
    ){
        reviewService.updateReview(reviewId, request.toDto(), userInfo);
        return ApiUtils.success(REVIEW_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{reviewId}")
    public GlobalResponse<String> deleteReview(
            @PathVariable(value = "reviewId") UUID reviewId,
            @AuthUser AuthUserInfo userInfo
    ){
        reviewService.deleteReview(reviewId, userInfo);
        return ApiUtils.success(REVIEW_DELETE_SUCCESS.getMessage());
    }
}
