package com.tk.gg.users.domain.repository.grade;

import com.tk.gg.users.domain.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
}
