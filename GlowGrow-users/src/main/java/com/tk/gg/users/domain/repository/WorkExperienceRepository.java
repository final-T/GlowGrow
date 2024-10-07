package com.tk.gg.users.domain.repository;

import com.tk.gg.users.domain.model.PreferStyle;
import com.tk.gg.users.domain.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, UUID> {
}
