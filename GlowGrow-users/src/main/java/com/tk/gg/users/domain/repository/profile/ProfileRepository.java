package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.Profile;
import com.tk.gg.users.infra.repository.profile.ProfileQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileQueryDslRepository {
    Optional<Profile> findByProfileIdAndIsDeletedFalse(UUID profileId);
    Optional<Profile> findByUserUserIdAndIsDeletedFalse(Long UserId);
    Optional<Profile> findByProfileIdAndUserUserIdAndIsDeletedFalse(UUID profileId, Long UserId);

    @EntityGraph(attributePaths = {"preferLocations", "preferPrices", "preferStyles", "awards", "workExperiences"})
    @Query("SELECT p FROM Profile p WHERE p.isDeleted = false")
    Page<Profile> findAllByIsDeletedFalse(Pageable pageable);
}
