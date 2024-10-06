package com.tk.gg.promotion.domain;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.promotion.domain.enums.CouponStatus;
import com.tk.gg.promotion.domain.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "p_coupons")
@Getter
@SQLRestriction("is_delete is false")
@SQLDelete(sql = "UPDATE p_coupons SET deleted_at = NOW() where coupon_id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coupon_id")
    private UUID couponId;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "max_discount", nullable = false)
    private BigDecimal maxDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Column(name = "total_qauantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete = false;

    @Builder
    public Coupon(Promotion promotion,
                  String code,
                  String description,
                  BigDecimal discountValue,
                  BigDecimal maxDiscount,
                  DiscountType discountType,
                  LocalDate validFrom,
                  LocalDate validUntil,
                  Integer totalQuantity
    ) {
        this.promotion = promotion;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.discountType = discountType;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.totalQuantity = totalQuantity;
    }

    public void issueCoupon() {
        if (this.totalQuantity <= 0) {
            throw new IllegalArgumentException("쿠폰 발급 가능 수량이 없습니다.");
        }
        this.totalQuantity--;
    }

    public void updateTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
