package com.tk.gg.users.domain.repository.profile;

import com.tk.gg.users.domain.model.PreferLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreferLocationRepository extends JpaRepository<PreferLocation, UUID> {
}
