package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Like;
import com.tk.gg.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findFirstByPostAndUserId(Post post, Long userId);
}
