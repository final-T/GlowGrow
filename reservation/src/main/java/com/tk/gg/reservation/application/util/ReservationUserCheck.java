package com.tk.gg.reservation.application.util;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.security.user.AuthUserInfo;

public class ReservationUserCheck {

    public static boolean canHandleReservation(Reservation reservation, AuthUserInfo userInfo) {
        if (userInfo.getUserRole().equals(UserRole.CUSTOMER)) {
            return reservation.getCustomerId().equals(userInfo.getId());
        } else if (userInfo.getUserRole().equals(UserRole.PROVIDER)) {
            return reservation.getServiceProviderId().equals(userInfo.getId());
        } else return userInfo.getUserRole().equals(UserRole.MASTER);
    }
}
