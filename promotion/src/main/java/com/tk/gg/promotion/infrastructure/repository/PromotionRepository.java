package com.tk.gg.promotion.infrastructure.repository;

import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.CouponStatus;
import com.tk.gg.promotion.domain.enums.PromotionStatus;
import com.tk.gg.promotion.domain.repository.PromotionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID>, PromotionRepositoryCustom {
    @Modifying
    @Query("UPDATE Promotion p SET p.status = :inactiveStatus " +
            "WHERE p.status = :activeStatus AND p.endDate < CURRENT_DATE")
    void updatePromotionStatus(@Param("inactiveStatus") PromotionStatus inactiveStatus, @Param("activeStatus") PromotionStatus activeStatus);

    @Modifying
    @Query("UPDATE Coupon c SET c.status = :expiredStatus " +
            "WHERE c.status = :activeStatus AND c.validUntil < CURRENT_DATE")
    void updateCouponStatus(@Param("expiredStatus") CouponStatus expiredStatus, @Param("activeStatus") CouponStatus activeStatus);

    @Modifying
    @Query("UPDATE Coupon c SET c.totalQuantity = c.totalQuantity - 1 WHERE c.couponId = :couponId AND c.totalQuantity > 0")
    int decreaseCouponQuantity(@Param("couponId") UUID couponId);
}