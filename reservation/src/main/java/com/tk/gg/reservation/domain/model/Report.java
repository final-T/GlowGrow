package com.tk.gg.reservation.domain.model;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.reservation.application.dto.UpdateReviewDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_reports")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "reporter_type")
    private UserRole reporterType;

    private String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void delete(String deletedBy){
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
