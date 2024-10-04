package com.tk.gg.post.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.dto.CommentRequestDto;
import com.tk.gg.post.application.dto.CommentResponseDto;
import com.tk.gg.post.application.dto.CommentSearchCondition;
import com.tk.gg.post.application.dto.CommentSearchResponseDto;
import com.tk.gg.post.domain.model.Comment;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponseDto writeComment(UUID postId, CommentRequestDto commentRequestDto) {
        Post post = postService.getPostById(postId);

        Comment parentComment = null;

        // 답글인경우
        if(commentRequestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findByCommentId(commentRequestDto.getParentCommentId())
                    .orElseThrow(() -> new GlowGlowException(GlowGlowError.COMMENT_NO_EXIST));

            // 부모 댓글이 삭제되었는지 확인
            if (parentComment.getIsDeleted()) {
                throw new GlowGlowException(GlowGlowError.COMMENT_DELETED);
            }

            // 댓글 깊이 제한 : 1단계
            if (parentComment.getParentComment() != null) { // 부모댓글이 없을 경우 답글
                throw new GlowGlowException(GlowGlowError.COMMENT_DEPTH_EXCEEDED);
            }

        }

        Comment comment = Comment.CreateCommentBuilder()
                .post(post)
                .parentComment(parentComment)
                .content(commentRequestDto.getContent())
                .build();

        comment.assignUserId(getCurrentUserId());

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.of(savedComment);
    }


    private Long getCurrentUserId() {
        return 1L; // 임시 반환값, 실제 구현에서는 인증된 사용자의 ID를 반환.
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(UUID commentId) {
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COMMENT_NO_EXIST));

        return CommentResponseDto.of(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(UUID commentId, CommentRequestDto commentRequestDto) {
        // TODO : 본인 확인 / 권한 체크
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COMMENT_NO_EXIST));

        comment.updateContent(commentRequestDto.getContent());
        return CommentResponseDto.of(comment);
    }


    @Transactional
    public void deleteComment(UUID commentId) {
        // TODO : 본인 확인 / 권한 체크
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COMMENT_NO_EXIST));

        comment.softDelete();
    }

    public Page<CommentSearchResponseDto> searchComment(Pageable pageable, CommentSearchCondition condition) {
        Page<CommentSearchResponseDto> result = commentRepository.searchComments(condition,pageable);
        if(result.isEmpty()){
            throw new GlowGlowException(GlowGlowError.NO_SEARCH_RESULTS);
        }
        return commentRepository.searchComments(condition,pageable);
    }

}
