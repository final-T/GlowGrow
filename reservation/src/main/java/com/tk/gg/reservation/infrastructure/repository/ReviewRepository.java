package com.tk.gg.reservation.infrastructure.repository;

import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.repository.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID>, ReviewRepositoryCustom {

    Boolean existsByReservationId(UUID reservationId);

    List<Review> findAllByReservationId(UUID reservationId);
}
