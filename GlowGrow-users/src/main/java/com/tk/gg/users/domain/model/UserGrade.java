package com.tk.gg.users.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "p_user_grades")
public class UserGrade extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_grade_id", nullable = false, updatable = false)
    private UUID userGradeId;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    private Grade grade;

    private Double score;
}
