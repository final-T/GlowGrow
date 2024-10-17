package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.PreferStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreferStyleRepository extends JpaRepository<PreferStyle, UUID> {
    Optional<PreferStyle> findByPreferStyleIdAndProfileProfileIdAndIsDeletedFalse(UUID preferStyleId, UUID profileId);
    boolean existsByProfileProfileIdAndIsDeletedFalseAndStyleName(UUID profileId, String styleName);
    List<PreferStyle> findAllByProfileProfileIdAndIsDeletedFalse(UUID profileId);
}
