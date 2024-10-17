package com.tk.gg.promotion.infrastructure.repository;

import com.tk.gg.promotion.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
}
