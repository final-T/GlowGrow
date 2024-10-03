package com.tk.gg.post.application.dto;

import com.tk.gg.post.domain.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private UUID commentId;
    private UUID postId;
    private Long userId;
    private String content;
    private UUID parentCommentId;
    private List<CommentResponseDto> childComments;


    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null)
                .childComments(comment.getChildComments().stream()
                        .filter(childComment -> !childComment.getIsDeleted()) // isDeleted가 false인 답글만 가져옴
                        .map(CommentResponseDto::of)
                        .collect(Collectors.toList()))
                .build();
    }

}
