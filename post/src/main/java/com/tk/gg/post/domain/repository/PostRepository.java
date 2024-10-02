package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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


    @Modifying
    @Query(value = "UPDATE p_posts SET likes = likes + CASE WHEN :likeStatus THEN 1 ELSE -1 END " +
            "WHERE post_id = :postId AND (likes > 0 OR :likeStatus = true)",
            nativeQuery = true)
    int updateLikeCount(@Param("postId") UUID postId, @Param("likeStatus") boolean likeStatus);

}
