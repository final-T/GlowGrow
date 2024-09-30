package com.tk.gg.promotion.domain;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.promotion.domain.enums.PromotionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_promotions")
@Getter
@SQLRestriction("is_delete is false")
@SQLDelete(sql = "UPDATE p_promotions SET deleted_at = NOW(), is_delete = true where promotion_id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Promotion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "promotion_id")
    private UUID promotionId;

    @OneToMany(mappedBy = "promotion")
    private List<Coupon> coupons = new ArrayList<>();

    @Column(name = "post_user_id", nullable = false)
    private Long postUserId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionStatus status;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete = false;

    @Builder
    public Promotion(UUID promotionId, Long postUserId, String title, String description, LocalDate startDate, LocalDate endDate, PromotionStatus status) {
        this.promotionId = promotionId;
        this.postUserId = postUserId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public void update(Promotion promotion) {
        this.title = promotion.title;
        this.description = promotion.description;
        this.startDate = promotion.startDate;
        this.endDate = promotion.endDate;
        this.status = promotion.status;
    }
}
