package com.tk.gg.reservation.domain.repository;


import com.tk.gg.reservation.domain.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID>, CustomTimeSlotRepository {
    Optional<TimeSlot> findByIdAndDeletedByNull(UUID reservationId);
    default Optional<TimeSlot> findByReservationId(UUID reservationId) {
        return findByIdAndDeletedByNull(reservationId);
    }

}
