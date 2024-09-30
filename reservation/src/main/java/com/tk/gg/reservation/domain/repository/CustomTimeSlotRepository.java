package com.tk.gg.reservation.domain.repository;

import com.tk.gg.reservation.domain.model.TimeSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface CustomTimeSlotRepository {
    Page<TimeSlot> searchTimeSlots(Date startDate, Date endDate, Pageable pageable);
}
