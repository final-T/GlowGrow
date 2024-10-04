package com.tk.gg.reservation.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.application.service.ReviewService;
import com.tk.gg.reservation.presentation.request.CreateReviewRequest;
import com.tk.gg.reservation.presentation.request.UpdateReviewRequest;
import com.tk.gg.reservation.presentation.response.ReviewResponse;
import com.tk.gg.reservation.presentation.response.ReviewWithReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//TODO : Security, 유저  검증
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public GlobalResponse<ReviewWithReservationResponse> createReview(
            @RequestBody @Valid CreateReviewRequest request
    ){
        return ApiUtils.success("리뷰 생성 성공",
                ReviewWithReservationResponse.from(reviewService.createReview(request.toDto()))
        );
    }

    @GetMapping
    public GlobalResponse<Page<ReviewResponse>> getAllReviews(
            ReviewSearchCondition searchCondition,
            @PageableDefault(sort = {"createdAt, updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiUtils.success("리뷰 전체 조회 성공",
                reviewService.searchReviews(searchCondition, pageable).map(ReviewResponse::from)
        );
    }

    @GetMapping("/{reviewId}")
    public GlobalResponse<ReviewWithReservationResponse> getOneReview(
            @PathVariable(value = "reviewId") UUID reviewId
    ){
        return ApiUtils.success("리뷰 단건 조회 성공",
                ReviewWithReservationResponse.from(reviewService.getOneReview(reviewId))
        );
    }

    @PutMapping("/{reviewId}")
    public GlobalResponse<String> updateReview(
            @PathVariable(value = "reviewId") UUID reviewId,
            @RequestBody @Valid UpdateReviewRequest request
    ){
        reviewService.updateReview(reviewId, request.toDto());
        return ApiUtils.success("리뷰 수정 성공");
    }

    @DeleteMapping("/{reviewId}")
    public GlobalResponse<String> deleteReview(
            @PathVariable(value = "reviewId") UUID reviewId
    ){
        //TODO : deletedBy 유저 수정
        reviewService.deleteReview(reviewId, "deletedBy");
        return ApiUtils.success("리뷰 삭제 성공");
    }
}
