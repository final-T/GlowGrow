package com.tk.gg.users.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.users.application.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "p_profiles")
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID profileId;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferLocation> preferLocations = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferPrice> preferPrices = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferStyle> preferStyles = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Award> awards = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences = new ArrayList<>();

    public static Profile create(User user, String profileImageUrl, String specialization, String bio) {
        return Profile.builder()
                .user(user)
                .profileImageUrl(profileImageUrl)
                .specialization(specialization)
                .bio(bio)
                .isDeleted(false)
                .build();
    }

    public void delete(UserDto user){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = user.username();

        // Soft delete associated lists
        this.preferLocations.forEach(PreferLocation::delete);
        this.preferPrices.forEach(PreferPrice::delete);
        this.preferStyles.forEach(PreferStyle::delete);
        this.awards.forEach(Award::delete);
        this.workExperiences.forEach(WorkExperience::delete);
    }
}
