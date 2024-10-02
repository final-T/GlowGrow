package com.tk.gg.promotion.domain;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "p_coupon_users")
@Getter
@SQLRestriction("is_delete is false")
@SQLDelete(sql = "UPDATE p_coupons_users SET deleted_at = NOW() where coupon_user_id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CouponUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coupon_user_id")
    private UUID couponUserId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete = false;

    @Builder
    public CouponUser(Coupon coupon, Long userId) {
        this.coupon = coupon;
        this.userId = userId;
    }
}
