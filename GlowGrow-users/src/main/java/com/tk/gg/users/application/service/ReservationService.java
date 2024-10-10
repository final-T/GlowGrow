package com.tk.gg.users.application.service;

import com.tk.gg.users.application.client.ReservationClient;
import com.tk.gg.users.presenation.response.GradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationClient reservationClient;

    public GradeResponse getGradeForReservation(Long userId, UUID reservationId){
        return reservationClient.getGradeForUserAndReservation(userId, reservationId).getData();
    }

    public GradeResponse getGradeForReview(Long userId, UUID reviewId) {
        return reservationClient.getGradeForUserAndReview(userId, reviewId).getData();
    }
}
