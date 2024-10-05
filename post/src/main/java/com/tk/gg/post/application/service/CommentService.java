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
import com.tk.gg.post.domain.service.CommentDomainService;
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
    private final CommentDomainService commentDomainService;

    @Transactional
    public CommentResponseDto writeComment(UUID postId, CommentRequestDto commentRequestDto) {
        Post post = postService.getPostById(postId);
        Comment parentComment = null;

        if(commentRequestDto.getParentCommentId() != null) {
            parentComment = getCommentById(commentRequestDto.getParentCommentId());
            commentDomainService.validateParentComment(parentComment);
        }

        Comment comment = commentDomainService.createComment(post, parentComment, commentRequestDto.getContent(), getCurrentUserId());
        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.of(savedComment);
    }

    private Long getCurrentUserId() {
        return 1L; // 임시 반환값, 실제 구현에서는 인증된 사용자의 ID를 반환.
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(UUID commentId) {
        Comment comment = getCommentById(commentId);
        return CommentResponseDto.of(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(UUID commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentById(commentId);
        commentDomainService.updateCommentContent(comment, commentRequestDto.getContent());
        return CommentResponseDto.of(comment);
    }

    @Transactional
    public void deleteComment(UUID commentId) {
        Comment comment = getCommentById(commentId);
        commentDomainService.softDeleteComment(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentSearchResponseDto> searchComment(Pageable pageable, CommentSearchCondition condition) {
        Page<CommentSearchResponseDto> result = commentRepository.searchComments(condition, pageable);
        if(result.isEmpty()){
            throw new GlowGlowException(GlowGlowError.NO_SEARCH_RESULTS);
        }
        return result;
    }

    private Comment getCommentById(UUID commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.COMMENT_NO_EXIST));
    }
}