package com.tk.gg.payment.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.payment.application.dto.SettlementDetailDto;
import com.tk.gg.payment.application.dto.SettlementDto;
import com.tk.gg.payment.application.service.SettlementService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "SETTLEMENT_ADMIN_CONTROLLER")
@Tag(name = "Settlement API", description = "정산 API")
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    // 정산 수동 처리 api
    @Operation(summary = "수동 정산 처리 API (MASTER)", description = "MASTER 권한이 수동으로 정산을 처리할 수 있는 API 입니다.")
    @PostMapping
    public GlobalResponse<SettlementDto.Response> createSettlement(
            @RequestBody SettlementDto.Request requestDto,
            @AuthUser AuthUserInfo authUserInfo
            ){
        SettlementDto.Response responseDto = settlementService.createSettlement(requestDto,authUserInfo);
        return ApiUtils.success(ResponseMessage.SETTLEMENT_CREATE_SUCCESS.getMessage(),responseDto);
    }

    // 단일 정산 조회 api
    @Operation(summary = "단일 정산 조회 API (MASTER)", description = "MASTER 권한이 단일 정산을 조회하는 API 입니다.")
    @GetMapping("/{settlementId}")
    public GlobalResponse<SettlementDto.Response> getSettlementById(
            @PathVariable UUID settlementId,
            @AuthUser AuthUserInfo authUserInfo
    ){
        SettlementDto.Response responseDto = settlementService.getSettlementById(settlementId,authUserInfo);
        return ApiUtils.success(ResponseMessage.SETTLEMENT_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    // 정산 업데이트 api
    @Operation(summary = "단일 정산 조회 API (MASTER)", description = "MASTER 권한이 정산 상태, 총 금액을 수정하는 API 입니다.")
    @PatchMapping("/{settlementId}")
    public GlobalResponse<SettlementDto.Response> updateSettlementById(
            @PathVariable UUID settlementId,
            @RequestBody SettlementDto.UpdateRequest requestDto,
            @AuthUser AuthUserInfo authUserInfo
    ){
        SettlementDto.Response responseDto = settlementService.updateSettlementById(settlementId,requestDto,authUserInfo);
        return ApiUtils.success(ResponseMessage.SETTLEMENT_UPDATE_SUCCESS.getMessage(),responseDto);
    }

    // 정산 검색 api
    @GetMapping("/search")
    @Operation(summary = "정산 검색 API (MASTER, PROVIDER)", description = "MASTER 권한은 모든 정산 검색 가능, PROVIDER는 자신의 정산만 확인할 수 있는 API 입니다.")
    public GlobalResponse<Page<SettlementDto.Response>> searchSettlements(
            @AuthUser AuthUserInfo authUserInfo,
            Pageable pageable,
            SettlementDto.SearchRequest requestDto
    ){
        Page<SettlementDto.Response> responsePage = settlementService.searchSettlements(pageable, requestDto, authUserInfo);
        return ApiUtils.success(ResponseMessage.SETTLEMENT_RETRIEVE_SUCCESS.getMessage(),responsePage);

    }

    // 정산 삭제 api
    @Operation(summary = "정산 삭제 API (MASTER)", description = "MASTER 권한이 정산 내역을 삭제하는 API 입니다.")
    @DeleteMapping("{settlementId}")
    public GlobalResponse<Void> deleteSettlement(
            @PathVariable UUID settlementId,
            @AuthUser AuthUserInfo authUserInfo
            ){
        settlementService.deleteSettlement(settlementId,authUserInfo);
        return ApiUtils.success(ResponseMessage.SETTLEMENT_DELETE_SUCCESS.getMessage(),null);

    }

    // 정산 세부사항 조회
    @Operation(summary = "정산 세부사항 조회 API (MASTER,PROVIDER)", description = "정산의 세부사항을 조회하는 API 입니다.")
    @GetMapping("/details")
    public GlobalResponse<List<SettlementDetailDto.Response>> getSettlementDetails(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestParam Long settlementTime
    ) {

        List<SettlementDetailDto.Response> details = settlementService.getSettlementDetailsByProviderAndTime(authUserInfo, settlementTime);
        return ApiUtils.success(ResponseMessage.SETTLEMENT_RETRIEVE_SUCCESS.getMessage(), details);
    }

}
