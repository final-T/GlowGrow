package com.tk.gg.reservation.application.service;


import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.application.dto.CreateReportDto;
import com.tk.gg.reservation.application.dto.ReportDto;
import com.tk.gg.reservation.domain.model.Report;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.service.ReportDomainService;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportDomainService reportDomainService;
    private final ReservationDomainService reservationDomainService;

    @Transactional
    public ReportDto createReport(CreateReportDto dto, AuthUserInfo userInfo) {
        Reservation reservation = reservationDomainService.getOne(dto.reservationId());
        Report report = reportDomainService.getByReportIdAndType(
                dto.userId(),dto.targetUserId()
        );
        // 이미 신고한 예약에 대해 같은 유저가 신고하면 에러
        if (report != null && userInfo.getId().equals(report.getUserId())){
            throw new GlowGlowException(GlowGlowError.REPORT_ALREADY_EXIST);
        }
        return ReportDto.from(reportDomainService.create(dto.toEntity(reservation,userInfo.getUserRole())));
    }

    @Transactional(readOnly = true)
    public Page<ReportDto> getAllReportsByUser(Long userId, AuthUserInfo userInfo, Pageable pageable){
        // 마스터 이외는 자신과 관련된 신고만 확인 가능
        if (!userInfo.getUserRole().equals(UserRole.MASTER) && !userId.equals(userInfo.getId())) {
            throw new GlowGlowException(GlowGlowError.REPORT_NOT_OWNER);
        }
        return reportDomainService.getAllByUser(userId, pageable).map(ReportDto::from);
    }

    @Transactional(readOnly = true)
    public ReportDto getReport(UUID reportId, AuthUserInfo userInfo) {
        Report report = reportDomainService.getOne(reportId);
        checkReportOwner(report, userInfo);
        return ReportDto.from(report);
    }

    @Transactional
    public void deleteReport(UUID reportId, AuthUserInfo userInfo) {
        Report report = reportDomainService.getOne(reportId);
        checkReportOwner(report, userInfo);
        reportDomainService.deleteOne(report, userInfo.getEmail());
    }

    private void checkReportOwner(Report report, AuthUserInfo userInfo) {
        if(!userInfo.getUserRole().equals(UserRole.MASTER)){
            if(!report.getUserId().equals(userInfo.getId()) ||
                    !report.getTargetUserId().equals(userInfo.getId())
            ) {
                throw new GlowGlowException(GlowGlowError.REPORT_NOT_OWNER);
            }
        }
    }
}
