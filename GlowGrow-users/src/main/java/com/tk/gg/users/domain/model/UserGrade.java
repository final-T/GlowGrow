package com.tk.gg.users.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.users.domain.type.UserGradeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @Enumerated(EnumType.STRING)
    private UserGradeType userGradeType;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    private Double score;

    public static UserGrade of(User user, UserGradeType userGradeType, Double score) {
        return UserGrade.builder()
                .user(user)
                .userGradeType(userGradeType)
                .score(score)
                .isDeleted(false)
                .build();
    }

    public void updateGrade(UserGradeType userGradeType, Double updateScore) {
        this.userGradeType = userGradeType;
        this.score = updateScore;
    }
}
