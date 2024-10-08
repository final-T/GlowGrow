package com.tk.gg.users.domain.repository.user;

import com.tk.gg.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserIdAndIsDeletedFalse(Long userId);
}
