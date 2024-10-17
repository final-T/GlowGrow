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
@Table(name = "p_prefer_styles")
public class PreferStyle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prefer_style_id", nullable = false, updatable = false)
    private UUID preferStyleId;

    @Column(name = "sytle_name", nullable = false)
    private String styleName;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public static PreferStyle create(Profile profile, String styleName) {
        return PreferStyle.builder()
                .profile(profile)
                .styleName(styleName)
                .isDeleted(false)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
