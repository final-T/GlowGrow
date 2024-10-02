package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> , PostRepositoryCustom {
    Optional<Post> findByPostIdAndIsDeletedFalse(UUID postId);
    default Optional<Post> findByPostId(UUID postId) {
        return findByPostIdAndIsDeletedFalse(postId);
    }

    @Modifying
    @Query(value = "UPDATE Post SET views = views + 1 WHERE postId = :postId")
    void incrementViews(@Param("postId") UUID postId);

    @Query(value = "SELECT views FROM Post WHERE postId = :postId")
    int getViews(@Param("postId") UUID postId);

}
