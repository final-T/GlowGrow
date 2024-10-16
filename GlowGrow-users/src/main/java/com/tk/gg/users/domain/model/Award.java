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
@Table(name = "p_awards")
public class Award extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "award_id", nullable = false, updatable = false)
    private UUID awardId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "award_name", nullable = false)
    private String awardName; // 수상 명

    @Column(name = "award_level", nullable = false)
    private String awardLevel; // 수상 수준

    @Column(name = "award_date", nullable = false)
    private String awardDate; // 수상 날짜

    @Column(name = "organization", nullable = false)
    private String organization; // 개최한 기관명

    @Setter
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public static Award create(Profile profile, String awardName, String awardLevel, String awardDate, String organization) {
        return Award.builder()
                .profile(profile)
                .awardName(awardName)
                .awardLevel(awardLevel)
                .awardDate(awardDate)
                .organization(organization)
                .isDeleted(false)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}

