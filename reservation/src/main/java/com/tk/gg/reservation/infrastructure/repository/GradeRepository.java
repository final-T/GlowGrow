package com.tk.gg.reservation.infrastructure.repository;

import com.tk.gg.reservation.domain.model.Grade;
import com.tk.gg.reservation.domain.repository.GradeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID>, GradeRepositoryCustom {
    Optional<Grade> findByUserIdAndReservationId(Long userId, UUID reservationId);
    Optional<Grade> findByUserIdAndReviewId(Long userId, UUID reviewId);
    Optional<Grade> findByUserId(Long userId);
}
