package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.Profile;
import com.tk.gg.users.domain.model.User;
import com.tk.gg.users.infra.repository.profile.ProfileQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileQueryDslRepository {
    Optional<Profile> findByProfileIdAndIsDeletedFalse(UUID profileId);
    Optional<Profile> findByUserUserIdAndIsDeletedFalse(Long UserId);
    Optional<Profile> findByProfileIdAndUserUserIdAndIsDeletedFalse(UUID profileId, Long UserId);
}
