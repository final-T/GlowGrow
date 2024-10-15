package com.tk.gg.reservation.domain.repository;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.security.user.AuthUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReservationRepositoryCustom {
    Page<Reservation> searchReservations(LocalDate startDate, LocalDate endDate, ReservationStatus status, Pageable pageable, AuthUserInfo userInfo);
}
