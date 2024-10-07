package com.tk.gg.users.domain.repository;

import com.tk.gg.users.domain.model.PreferStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreferStyleRepository extends JpaRepository<PreferStyle, UUID> {
}
