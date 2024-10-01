package com.tk.gg.reservation.domain.model;


import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_time_slots")
public class TimeSlot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private Long serviceProviderId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate availableDate;

    @Column(nullable = false)
    private Integer availableTime;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isReserved = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(id, timeSlot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void update(UpdateTimeSlotRequestDto dto){
        this.serviceProviderId = dto.serviceProviderId();
        this.availableDate = dto.availableDate();
        this.availableTime = dto.availableTime();
        this.isReserved = dto.isReserved();
    }

    public void delete(String deletedBy){
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
