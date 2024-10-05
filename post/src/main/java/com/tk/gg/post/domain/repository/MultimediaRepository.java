package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MultimediaRepository extends JpaRepository<Multimedia, UUID>, MultimediaRepositoryCustom {
    Optional<Multimedia> findByMultiMediaIdAndIsDeletedFalse(UUID multiMediaId);
    default Optional<Multimedia> findByMultiMediaId(UUID multiMediaId) {
        return findByMultiMediaIdAndIsDeletedFalse(multiMediaId);
    }
}
