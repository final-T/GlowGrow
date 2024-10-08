package com.tk.gg.reservation.infrastructure.repository;

import com.tk.gg.reservation.domain.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    Optional<Report> findByUserIdAndTargetUserId(Long userId,Long targetUserId);

    @Query("SELECT DISTINCT r FROM Report r WHERE r.userId = :userId OR r.targetUserId = :userId")
    Page<Report> findAllWithUserId(Long userId, Pageable pageable);
}
