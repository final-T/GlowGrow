package com.tk.gg.reservation.infrastructure.repository;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.repository.ReservationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID>, ReservationRepositoryCustom {
    Optional<Reservation> findByIdAndDeletedByNull(UUID reservationId);
    default Optional<Reservation> findByReservationId(UUID reservationId) {
        return findByIdAndDeletedByNull(reservationId);
    }
}
