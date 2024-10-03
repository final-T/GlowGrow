package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> , CommentRepositoryCustom {
    Optional<Comment> findByCommentIdAndIsDeletedFalse(UUID commentId);
    default Optional<Comment> findByCommentId(UUID commentId) {
        return findByCommentIdAndIsDeletedFalse(commentId);
    }
}
