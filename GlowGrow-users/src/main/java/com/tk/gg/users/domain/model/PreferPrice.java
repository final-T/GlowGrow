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
@Table(name = "p_prefer_prices")
public class PreferPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prefer_price_id", nullable = false, updatable = false)
    private UUID preferPriceId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "price_id", nullable = false)
    private UUID priceId;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static PreferPrice create(Profile profile, UUID priceId) {
        return PreferPrice.builder()
                .profile(profile)
                .priceId(priceId)
                .isDeleted(false)
                .build();
    }
}
