package com.tk.gg.reservation.presentation.request;


import java.util.UUID;

public record ReviewSearchCondition(
        UUID reservationId,
        Long reviewerId,
        Long targetUserId
) { }
