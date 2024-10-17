package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AwardRepository extends JpaRepository<Award, UUID> {
    Optional<Award> findByAwardIdAndProfileProfileId(UUID userId, UUID profileId);
    boolean existsByAwardNameAndAwardLevelAndAwardDateAndOrganization(String awardName, String awardLevel, String awardDate, String organization);
    List<Award> findAllByProfileProfileIdAndIsDeletedFalse(UUID profileId);
}
