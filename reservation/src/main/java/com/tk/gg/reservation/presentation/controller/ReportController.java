package com.tk.gg.reservation.presentation.controller;


import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.ReportService;
import com.tk.gg.reservation.presentation.request.CreateReportRequest;
import com.tk.gg.reservation.presentation.response.ReportResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report API", description = "예약에 대한 신고 기능")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "생성 API", description = "신고(report) 정보를 생성합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<ReportResponse> createReport(
        @RequestBody @Valid CreateReportRequest request,
        @AuthUser AuthUserInfo userInfo
    ){
        return ApiUtils.success(REPORT_CREATE_SUCCESS.getMessage(),
                ReportResponse.from(reportService.createReport(request.toDto(), userInfo))
        );
    }

    @GetMapping("'/users/{userId}")
    @Operation(summary = "전체 조회 API", description = "신고(report) 목록을 조회합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<Page<ReportResponse>> getAllReportsByUser(
            @PathVariable(value = "userId") Long userId,
            @ParameterObject  @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthUser AuthUserInfo userInfo
    ){
        return ApiUtils.success(REPORT_RETRIEVE_SUCCESS.getMessage(),
                reportService.getAllReportsByUser(userId,userInfo, pageable).map(ReportResponse::from)
        );
    }


    @GetMapping("/{reportId}")
    @Operation(summary = "단건 조회 API", description = "신고(report) 정보를 조회합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<ReportResponse> getReport(
            @PathVariable(value = "reportId") UUID reportId,
            @AuthUser AuthUserInfo userInfo
    ){
        return ApiUtils.success(REPORT_RETRIEVE_SUCCESS.getMessage(),
               ReportResponse.from(reportService.getReport(reportId, userInfo)));
    }

    @DeleteMapping("/{reportId}")
    @Operation(summary = "삭제 API", description = "신고(report) 정보를 삭제합니다.**[ROLE: Provider,Customer,Master]**")
    public GlobalResponse<String> deleteReport(
            @PathVariable(value = "reportId") UUID reportId,
            @AuthUser AuthUserInfo userInfo
    ){
        reportService.deleteReport(reportId, userInfo);
        return ApiUtils.success(REPORT_DELETE_SUCCESS.getMessage());
    }
}
