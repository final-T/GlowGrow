package com.tk.gg.reservation.infrastructure.repository;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.repository.ReservationRepositoryCustom;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID>, ReservationRepositoryCustom {
    Optional<Reservation> findByIdAndDeletedByNull(UUID reservationId);
    default Optional<Reservation> findByReservationId(UUID reservationId) {
        return findByIdAndDeletedByNull(reservationId);
    }

    List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus);

    @Query("SELECT r FROM Reservation r WHERE r.reservationStatus = :status")
    List<Reservation> findAllByReservationStatusWithLimit(@Param("status") ReservationStatus status, Pageable pageable);
}
