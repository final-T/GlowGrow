package com.tk.gg.users.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.presenation.request.UpdateProfileRequest;
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
    @Column(name = "profile_id", nullable = false, updatable = false)
    private UUID profileId;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Setter
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Setter
    @Column(name = "specialization")
    private String specialization;

    @Setter
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferLocation> preferLocations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferPrice> preferPrices = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferStyle> preferStyles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Award> awards = new ArrayList<>();

    @Builder.Default
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

    public void update(UserDto user, UpdateProfileRequest request) {
        Optional.ofNullable(request.profileImageUrl()).ifPresent(url -> this.profileImageUrl = url);
        Optional.ofNullable(request.specialization()).ifPresent(spec -> this.specialization = spec);
        Optional.ofNullable(request.bio()).ifPresent(bio -> this.bio = bio);
        this.updatedBy = user.username();
        this.updatedAt = LocalDateTime.now();
    }
}
