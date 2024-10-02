package com.tk.gg.reservation.application.service;

import com.tk.gg.reservation.application.dto.CreateReservationDto;
import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.service.ReservationDomainService;
import com.tk.gg.reservation.domain.service.TimeSlotDomainService;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDomainService reservationDomainService;

    @Mock
    private TimeSlotDomainService timeSlotDomainService;

    @InjectMocks
    private ReservationService reservationService;

    private TimeSlot timeSlot;
    private Long serviceProviderId;
    private Long customerId;

    @BeforeEach
    void setUp() {
        serviceProviderId = 1L;
        customerId = 10L;
        timeSlot = TimeSlot.builder()
                .id(UUID.randomUUID())
                .serviceProviderId(serviceProviderId)
                .availableDate(LocalDate.parse("2024-10-02", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .availableTime(20)
                .isReserved(false)
                .build();
    }

    @Test
    void createReservation_ShouldSetTimeSlotAsReserved() {
        // Given
        CreateReservationDto createReservationDto = CreateReservationDto.builder()
                .timeSlotId(timeSlot.getId()).serviceProviderId(serviceProviderId)
                .reservationDate(LocalDate.parse("2024-10-02", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .reservationTime(20).customerId(customerId)
                .build();
        Reservation reservation = Reservation.builder().id(UUID.randomUUID()).timeSlot(timeSlot)
                .serviceProviderId(serviceProviderId).customerId(customerId)
                .reservationDate(LocalDate.parse("2024-10-02", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .reservationTime(20).reservationStatus(ReservationStatus.CHECK).build();
        when(timeSlotDomainService.getOne(any(UUID.class))).thenReturn(timeSlot);
        when(reservationDomainService.create(any(CreateReservationDto.class),
                any(TimeSlot.class))).thenAnswer(invocation -> {
            TimeSlot ts = invocation.getArgument(1);
            ts.updateIsReserved(true);
            return reservation;
        });

        // When
        ReservationDto result = reservationService.createReservation(createReservationDto);

        // Then
        assertNotNull(result);
        assertTrue(reservation.getTimeSlot().getIsReserved());
        verify(reservationDomainService).create(any(CreateReservationDto.class), any(TimeSlot.class));
    }
}