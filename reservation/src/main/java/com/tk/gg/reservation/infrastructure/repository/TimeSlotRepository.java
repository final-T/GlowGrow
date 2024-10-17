package com.tk.gg.reservation.infrastructure.repository;


import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.repository.TimeSlotRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID>, TimeSlotRepositoryCustom {
    Optional<TimeSlot> findByIdAndDeletedByNull(UUID reservationId);
    default Optional<TimeSlot> findByReservationId(UUID reservationId) {
        return findByIdAndDeletedByNull(reservationId);
    }

    Optional<TimeSlot> findByAvailableDateEqualsAndAvailableTimeEquals(LocalDate availableDate, Integer availableTime);
}
