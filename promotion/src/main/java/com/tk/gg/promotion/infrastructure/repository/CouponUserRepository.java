package com.tk.gg.promotion.infrastructure.repository;

import com.tk.gg.promotion.domain.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUser, UUID> {
    @Query("SELECT COUNT(cu) > 0 FROM CouponUser cu WHERE cu.coupon.couponId = :couponId AND cu.userId = :userId")
    boolean existsByCouponAndUserId(UUID couponId, Long userId);

    @Query("SELECT cu FROM CouponUser cu WHERE cu.userId = :userId and cu.isUsed = false")
    List<CouponUser> findByUserId(Long userId);

    @Query("SELECT cu FROM CouponUser cu WHERE cu.userId = :userId AND cu.coupon.couponId = :couponId")
    Optional<CouponUser> findByUserIdAndCouponId(Long userId, UUID couponId);

    @Query("SELECT COUNT(cu) FROM CouponUser cu WHERE cu.coupon.couponId = :couponId")
    int countByCouponId(UUID couponId);
}
