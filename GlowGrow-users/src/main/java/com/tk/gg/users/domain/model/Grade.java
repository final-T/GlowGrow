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
@Table(name = "p_grades")
public class Grade extends BaseEntity {
    @Id
    @Column(name = "grade_id", nullable = false, updatable = false)
    private UUID gradeId;

    @Setter
    @Column(name = "grade_name", nullable = false, unique = true)
    private String gradeName;

    @Setter
    @Column(name = "grade_min_score", nullable = false)
    private Double gradeMinScore;

    @Setter
    @Column(name = "grade_max_score", nullable = false)
    private Double gradeMaxScore;

    @Setter
    @Column(name = "grade_image_url", nullable = false)
    private String gradeImageUrl;

    @Setter
    @Column(name = "grade_info")
    private String gradeInfo;

    public static Grade of(UUID gradeId, String gradeName, Double gradeMinScore, Double gradeMaxScore, String gradeImageUrl, String gradeInfo) {
        return new Grade(gradeId, gradeName, gradeMinScore, gradeMaxScore, gradeImageUrl, gradeInfo);
    }
}
