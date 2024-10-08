package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.PreferPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreferPriceRepository extends JpaRepository<PreferPrice, UUID> {
    Optional<PreferPrice> findByPreferPriceIdAndProfileProfileIdAndIsDeletedFalse(UUID preferPriceId, UUID profileId);
    boolean existsByProfileProfileIdAndIsDeletedFalseAndPrice(UUID profileId, Long price);

    List<PreferPrice> findAllByProfileProfileIdAndIsDeletedFalse(UUID profileId);
}
