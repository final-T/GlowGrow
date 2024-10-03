package com.tk.gg.post.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder
public class CommentSearchResponseDto {
    private UUID commentId;
    private UUID postId;
    private Long userId;
    private String content;
    private UUID parentCommentId;

    public CommentSearchResponseDto(UUID commentId, UUID postId, Long userId, String content, UUID parentCommentId) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }
}
