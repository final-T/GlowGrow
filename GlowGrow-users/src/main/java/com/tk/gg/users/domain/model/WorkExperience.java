package com.tk.gg.users.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "p_work_experiences")
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "work_experience_id", nullable = false, updatable = false)
    private UUID work_experience_id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static WorkExperience create(Profile profile, String companyName, String position, Integer experience) {
        return WorkExperience.builder()
                .profile(profile)
                .companyName(companyName)
                .position(position)
                .experience(experience)
                .isDeleted(false)
                .build();
    }
}
