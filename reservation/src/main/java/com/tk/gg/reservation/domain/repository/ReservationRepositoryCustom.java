package com.tk.gg.reservation.domain.repository;

import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.presentation.request.ReservationSearchCondition;
import com.tk.gg.security.user.AuthUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReservationRepositoryCustom {
    Page<Reservation> searchReservations(ReservationSearchCondition searchCondition, Pageable pageable, AuthUserInfo userInfo);
}
