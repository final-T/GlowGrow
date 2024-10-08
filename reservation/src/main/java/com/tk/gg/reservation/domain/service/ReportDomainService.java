package com.tk.gg.reservation.domain.service;


import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.reservation.domain.model.Report;
import com.tk.gg.reservation.infrastructure.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReportDomainService {

    private final ReportRepository reportRepository;

    public Report create(Report newReport) {
        return reportRepository.save(newReport);
    }

    public Page<Report> getAllByUser(Long userId, Pageable pageable) {
        return reportRepository.findAllWithUserId(userId, pageable);
    }


    public Report getOne(UUID reportId) {
        return reportRepository.findById(reportId).orElseThrow(
                () -> new GlowGlowException(GlowGlowError.REPORT_NO_EXIST)
        );
    }

    public void deleteOne(Report report, String deletedBy) {
        report.delete(deletedBy);
    }

    public Report getByReportIdAndType(Long userId, Long targetId){
        return reportRepository.findByUserIdAndTargetUserId(userId, targetId).orElse(null);
    }

}
