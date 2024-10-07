package com.tk.gg.reservation.presentation.controller;


import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.application.service.ReportService;
import com.tk.gg.reservation.presentation.request.CreateReportRequest;
import com.tk.gg.reservation.presentation.response.ReportResponse;
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
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public GlobalResponse<ReportResponse> createReport(
        @RequestBody @Valid CreateReportRequest request,
        @AuthUser AuthUserInfo userInfo
    ){
        return ApiUtils.success(REPORT_CREATE_SUCCESS.getMessage(),
                ReportResponse.from(reportService.createReport(request.toDto(), userInfo))
        );
    }

    @GetMapping("'/users/{userId}")
    public GlobalResponse<Page<ReportResponse>> getAllReportsByUser(
            @PathVariable(value = "userId") Long userId,
            @AuthUser AuthUserInfo userInfo,
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiUtils.success(REPORT_RETRIEVE_SUCCESS.getMessage(),
                reportService.getAllReportsByUser(userId,userInfo, pageable).map(ReportResponse::from)
        );
    }


    @GetMapping("/{reportId}")
    public GlobalResponse<ReportResponse> getReport(
            @PathVariable(value = "reportId") UUID reportId,
            @AuthUser AuthUserInfo userInfo
    ){
        return ApiUtils.success(REPORT_RETRIEVE_SUCCESS.getMessage(),
               ReportResponse.from(reportService.getReport(reportId, userInfo)));
    }

    @DeleteMapping("/{reportId}")
    public GlobalResponse<String> deleteReport(
            @PathVariable(value = "reportId") UUID reportId,
            @AuthUser AuthUserInfo userInfo
    ){
        reportService.deleteReport(reportId, userInfo);
        return ApiUtils.success(REPORT_DELETE_SUCCESS.getMessage());
    }
}
