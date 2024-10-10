package com.tk.gg.reservation.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.reservation.application.dto.*;
import com.tk.gg.reservation.application.service.ReviewService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.presentation.request.CreateReviewRequest;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.presentation.request.UpdateReviewRequest;
import com.tk.gg.reservation.presentation.response.ReviewResponse;
import com.tk.gg.reservation.presentation.response.ReviewWithReservationResponse;
import com.tk.gg.security.user.AuthUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Review] - ReviewController")
@AutoConfigureMockMvc
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("[POST] 리뷰 생성 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 리뷰_생성_성공() throws Exception {
        CreateReviewRequest request = new CreateReviewRequest(
                UUID.randomUUID(),       // 예약 ID
                1L,                     // 리뷰 작성자 ID
                2L,                     // 리뷰 대상자 ID
                5,                      // 평점 (예: 1~5)
                "정말 좋은 서비스,정말 좋은 서비스,정말 좋은 서비스", // 리뷰 내용
                5,
                3,
                4,
                3,
                5,
                null,
                null,
                null,
                null
        );
        ReviewWithReservationDto reviewWithReservationDto = createReviewWithReservationDto();

        given(reviewService.createReview(any(CreateReviewDto.class), any(AuthUserInfo.class)))
                .willReturn(reviewWithReservationDto);

        mvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(REVIEW_CREATE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").exists())
                .andDo(print());

        then(reviewService).should().createReview(any(CreateReviewDto.class), any(AuthUserInfo.class));
    }

    @DisplayName("[GET] 모든 리뷰 조회 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 모든_리뷰_조회_성공() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        List<ReviewDto> reviews = List.of(createReviewDto()); // 필요한 필드로 초기화

        given(reviewService.searchReviews(any(ReviewSearchCondition.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(reviews, pageable, reviews.size()));

        mvc.perform(get("/api/reviews")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(REVIEW_RETRIEVE_SUCCESS.getMessage()))
                .andDo(print());

        then(reviewService).should().searchReviews(any(ReviewSearchCondition.class), any(Pageable.class));
    }

    @DisplayName("[GET] 리뷰 단건 조회 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 리뷰_단건_조회_성공() throws Exception {
        UUID reviewId = UUID.randomUUID();
        ReviewWithReservationDto reviewWithReservationDto = createReviewWithReservationDto();

        given(reviewService.getOneReview(reviewId)).willReturn(reviewWithReservationDto);

        mvc.perform(get("/api/reviews/{reviewId}", reviewId)
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(REVIEW_RETRIEVE_SUCCESS.getMessage()))
                .andDo(print());

        then(reviewService).should().getOneReview(reviewId);
    }

    @DisplayName("[PUT] 리뷰 수정 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 리뷰_수정_성공() throws Exception {
        UUID reviewId = UUID.randomUUID();
        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(4, "\"좋은 서비스,좋은 서비스,좋은 서비스\"");

        ReviewWithReservationDto reviewWithReservationDto = createReviewWithReservationDto();

        willDoNothing().given(reviewService).updateReview(eq(reviewId), any(UpdateReviewDto.class), any(AuthUserInfo.class));

        mvc.perform(put("/api/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReviewRequest))
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(REVIEW_UPDATE_SUCCESS.getMessage()))
                .andDo(print());

        then(reviewService).should().updateReview(eq(reviewId), any(UpdateReviewDto.class), any(AuthUserInfo.class));
    }

    @DisplayName("[DELETE] 리뷰 삭제 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 리뷰_삭제_성공() throws Exception {
        UUID reviewId = UUID.randomUUID();

        mvc.perform(delete("/api/reviews/{reviewId}", reviewId)
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(REVIEW_DELETE_SUCCESS.getMessage()))
                .andDo(print());

        then(reviewService).should().deleteReview(eq(reviewId), any(AuthUserInfo.class));
    }



    private ReviewDto createReviewDto(){
        return ReviewDto.builder()
                .id(UUID.randomUUID())
                .reviewerId(1L)
                .targetUserId(2L)
                .rating(3)
                .content("테스트 리뷰")
                .build();
    }

    private ReviewWithReservationDto createReviewWithReservationDto(){
        return ReviewWithReservationDto.builder()
                .id(UUID.randomUUID())
                .reservationDto(createReservationDto())
                .reviewerId(1L)
                .targetUserId(2L)
                .rating(3)
                .content("테스트 리뷰")
                .build();
    }

    private ReservationDto createReservationDto() {
        return ReservationDto.builder()
                .id(UUID.randomUUID())
                .timeSlotDto(createTimeSlotDto())
                .reservationDate(LocalDate.parse("2024-10-07"))
                .reservationTime(20)
                .customerId(1L)
                .serviceProviderId(2L)
                .reservationStatus(ReservationStatus.CHECK)
                .price(0)
                .build();
    }

    private TimeSlotDto createTimeSlotDto(){
        return TimeSlotDto.builder()
                .id(UUID.randomUUID())
                .availableDate(LocalDate.parse("2024-10-07"))
                .serviceProviderId(2L)
                .isReserved(false)
                .availableTime(20).build();
    }
}