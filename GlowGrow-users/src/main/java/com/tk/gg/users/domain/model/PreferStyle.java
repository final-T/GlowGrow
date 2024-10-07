package com.tk.gg.users.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "p_prefer_styles")
public class PreferStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prefer_style_id", nullable = false, updatable = false)
    private UUID preferStyleId;

    @Column(name = "sytle_id", nullable = false)
    private UUID styleId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
