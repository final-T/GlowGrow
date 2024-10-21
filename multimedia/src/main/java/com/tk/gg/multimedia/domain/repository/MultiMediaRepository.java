package com.tk.gg.multimedia.domain.repository;

import com.tk.gg.multimedia.domain.model.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MultiMediaRepository extends JpaRepository<Multimedia, UUID> {
    Optional<Multimedia> findByUserIdAndMultiMediaIdAndIsDeletedFalse(Long userId, UUID multiMediaId);
}
