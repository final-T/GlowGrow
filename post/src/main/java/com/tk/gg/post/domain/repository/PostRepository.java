package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByPostIdAndIsDeletedFalse(UUID postId);
    default Optional<Post> findByPostId(UUID postId) {
        return findByPostIdAndIsDeletedFalse(postId);
    }

}
