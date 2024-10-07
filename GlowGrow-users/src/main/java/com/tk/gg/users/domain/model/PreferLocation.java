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

    @Column(name = "location_id", nullable = false)
    private UUID locationId;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
