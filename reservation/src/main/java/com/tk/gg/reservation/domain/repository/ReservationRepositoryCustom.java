package com.tk.gg.reservation.domain.repository;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReservationRepositoryCustom {
    Page<Reservation> searchReservations(LocalDate startDate, LocalDate endDate, ReservationStatus status, Pageable pageable);
}
