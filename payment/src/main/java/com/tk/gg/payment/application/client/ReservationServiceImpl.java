package com.tk.gg.payment.application.client;

import com.tk.gg.common.kafka.payment.PaymentReservationResponseDto;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.payment.infrastructure.client.ReservationFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationFeignClient reservationFeignClient;


    @Override
    public PaymentReservationResponseDto getOneReservation(UUID reservationId, String authToken) {
        GlobalResponse<PaymentReservationResponseDto> response = reservationFeignClient.getOneReservation(
                reservationId,
                "Bearer " + authToken
        );
        return response.getData();
    }
}
