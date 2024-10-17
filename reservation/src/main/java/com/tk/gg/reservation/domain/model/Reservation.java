package com.tk.gg.reservation.domain.model;


import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_reservations")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    private TimeSlot timeSlot;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long serviceProviderId;

    @Column(name = "reservationStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private Integer reservationTime;

    @Column(nullable = false)
    private Integer price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void update(UpdateReservationDto dto, TimeSlot timeSlot){
        this.timeSlot = timeSlot;
        this.reservationDate = dto.reservationDate();
        this.reservationTime = dto.reservationTime();
        this.price = dto.price();
    }

    public void updateStatus(ReservationStatus status){
        this.reservationStatus = status;
    }

    public void delete(String deletedBy){
        this.timeSlot = null; // 연결 끊기
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
