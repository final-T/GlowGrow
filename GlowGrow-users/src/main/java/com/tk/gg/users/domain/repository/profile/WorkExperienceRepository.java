package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, UUID> {
    Optional<WorkExperience> findByWorkExperienceIdAndProfileProfileIdAndIsDeletedFalse(UUID workExperienceId, UUID profileId);
    boolean existsByProfileProfileIdAndIsDeletedFalseAndCompanyName(UUID profileId, String companyName);

    List<WorkExperience> findAllByProfileProfileIdAndIsDeletedFalse(UUID profileId);
}
