package com.tk.gg.promotion.domain;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_promotions")
@Getter
@SQLRestriction("is_delete is false")
@SQLDelete(sql = "UPDATE p_promotions SET deleted_at = NOW() where promotion_id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Promotion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "promotion_id")
    private UUID promotionId;

    @OneToMany(mappedBy = "promotion")
    private List<Coupon> coupons = new ArrayList<>();

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete = false;

    @Builder
    public Promotion(String title,
                     String description,
                     LocalDateTime startDate,
                     LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
