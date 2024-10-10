package com.tk.gg.users.domain.repository.grade;

import com.tk.gg.users.domain.model.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserGradeRepository extends JpaRepository<UserGrade, UUID> {
    Optional<UserGrade> findByUserUserId(Long userId);
}
