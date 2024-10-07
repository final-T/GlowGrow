package com.tk.gg.users.domain.repository;

import com.tk.gg.users.domain.model.PreferPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreferPriceRepository extends JpaRepository<PreferPrice, UUID> {
}
