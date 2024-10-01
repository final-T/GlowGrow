package com.tk.gg.reservation.domain.repository;

import com.tk.gg.reservation.domain.model.TimeSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TimeSlotRepositoryCustom {
    Page<TimeSlot> searchTimeSlots(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
