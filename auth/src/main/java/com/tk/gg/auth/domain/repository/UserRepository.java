package com.tk.gg.auth.domain.repository;

import com.tk.gg.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
