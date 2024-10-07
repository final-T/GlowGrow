package com.tk.gg.users.domain.repository;

import com.tk.gg.users.domain.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
}
