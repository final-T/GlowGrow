package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.PreferLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreferLocationRepository extends JpaRepository<PreferLocation, UUID> {
    Optional<PreferLocation> findByPreferLocationIdAndProfileProfileIdAndIsDeletedFalse(UUID preferLocationId, UUID profileId);
    boolean existsByProfileProfileIdAndIsDeletedFalseAndLocationName(UUID profileId, String locationName);

    List<PreferLocation> findAllByProfileProfileIdAndIsDeletedFalse(UUID profileId);
}
