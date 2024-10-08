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
@Table(name = "p_prefer_locations")
public class PreferLocation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prefer_location_id", nullable = false, updatable = false)
    private UUID preferLocationId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static PreferLocation create(Profile profile, String locationName) {
        return PreferLocation.builder()
                .profile(profile)
                .locationName(locationName)
                .isDeleted(false)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
