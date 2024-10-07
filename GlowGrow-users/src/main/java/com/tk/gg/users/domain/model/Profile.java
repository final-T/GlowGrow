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


    public static Profile create(User user, String profileImageUrl, String specialization, String bio) {
        return Profile.builder()
                .user(user)
                .profileImageUrl(profileImageUrl)
                .specialization(specialization)
                .bio(bio)
                .isDeleted(false)
                .build();
    }
}
