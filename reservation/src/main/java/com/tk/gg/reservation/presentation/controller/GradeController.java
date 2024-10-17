package com.tk.gg.reservation.presentation.controller;


import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.dto.ResultGradeDto;
import com.tk.gg.reservation.application.service.GradeService;
import com.tk.gg.reservation.presentation.response.GradeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.GRADE_RETRIEVE_SUCCESS;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade API", description = "유저 평가정보 기능, 내부 서비스에서 사용")
public class GradeController {

    private final GradeService gradeService;

    //기능 필요성 검증
    @Deprecated(forRemoval = true)
    @GetMapping()
    @Operation(deprecated = true, hidden = true)
    public GlobalResponse<ResultGradeDto> getTotalGradeForUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "userType") UserRole userType,
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiUtils.success(GRADE_RETRIEVE_SUCCESS.getMessage(),
                gradeService.getUserGradeSummary(userId, userType, pageable));
    }

    @GetMapping("/users/{userId}/reservations/{reservationId}")
    @Operation(hidden = true)
    public GlobalResponse<GradeResponse> getGradeForUserAndReservation(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "reservationId") UUID reservationId
    ) {
        return ApiUtils.success(GRADE_RETRIEVE_SUCCESS.getMessage(),
                GradeResponse.from(gradeService.getGradeForUserAndReservation(userId, reservationId))
        );
    }

    @GetMapping("/users/{userId}/reviews/{reviewId}")
    @Operation(hidden = true)
    public GlobalResponse<GradeResponse> getGradeForUserAndReview(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "reviewId") UUID reviewId
    ) {
        return ApiUtils.success(GRADE_RETRIEVE_SUCCESS.getMessage(),
                GradeResponse.from(gradeService.getGradeForUserAndReview(userId, reviewId))
        );
    }
}
