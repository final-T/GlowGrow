package com.tk.gg.post.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.client.UserService;
import com.tk.gg.post.application.dto.CommentRequestDto;
import com.tk.gg.post.application.dto.CommentResponseDto;
import com.tk.gg.post.application.dto.CommentSearchCondition;
import com.tk.gg.post.application.dto.CommentSearchResponseDto;
import com.tk.gg.post.domain.model.Comment;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.CommentRepository;
import com.tk.gg.post.domain.service.CommentDomainService;
import com.tk.gg.post.infrastructure.client.UserFeignClient;
import com.tk.gg.security.user.AuthUserInfo;
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
    private final UserService userService;

    @Transactional
    public CommentResponseDto writeComment(UUID postId, CommentRequestDto commentRequestDto, AuthUserInfo authUserInfo) {
        Post post = postService.getPostById(postId);
        Comment parentComment = null;

        // 부모 댓글의 id가 있으면 -> 댓글
        if(commentRequestDto.getParentCommentId() != null) {
            parentComment = getCommentById(commentRequestDto.getParentCommentId());
            // 댓글이 삭제되었는 지 검증
            commentDomainService.validateParentComment(parentComment);
        }

        Comment comment = commentDomainService.createComment(post, parentComment, commentRequestDto.getContent(), authUserInfo);
        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.of(savedComment);
    }


    @Transactional(readOnly = true)
    public CommentResponseDto getComment(UUID commentId) {
        Comment comment = getCommentById(commentId);
        return CommentResponseDto.of(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(UUID commentId, CommentRequestDto commentRequestDto, AuthUserInfo authUserInfo) {
        Comment comment = getCommentById(commentId);

        // 사용자 존재 여부 체크
        checkUserExists(authUserInfo);

        // 권한 체크 -> (Provider,Customer)본인의 댓글만 수정 가능
        checkPermission(comment, authUserInfo);

        commentDomainService.updateCommentContent(comment, commentRequestDto.getContent(), authUserInfo);
        return CommentResponseDto.of(comment);
    }

    @Transactional
    public void deleteComment(UUID commentId, AuthUserInfo authUserInfo) {
        Comment comment = getCommentById(commentId);

        // 사용자 존재 여부 체크
        checkUserExists(authUserInfo);

        // 권한 체크 -> (Provider,Customer)본인의 댓글만 삭제 가능
        checkPermission(comment, authUserInfo);

        commentDomainService.softDeleteComment(comment,authUserInfo);
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

    // 사용자 존재 여부 확인 메서드
    private void checkUserExists(AuthUserInfo authUserInfo) {
        // Feign Client 사용하여 사용자 존재 여부 확인
        boolean userExists = userService.isUserExistsByEmail(authUserInfo.getEmail());
        if (!userExists) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }
    }

    // 권한 체크 메서드
    private void checkPermission(Comment comment, AuthUserInfo authUserInfo) {
        UserRole userRole = authUserInfo.getUserRole();

        if (UserRole.MASTER.equals(userRole)) {
            return; // MASTER 권한은 모든 작업 허용
        }

        // 사용자 권한 확인
        if (UserRole.CUSTOMER.equals(userRole) || UserRole.PROVIDER.equals(userRole)) {
            if(!comment.getUserId().equals(authUserInfo.getId())){
                log.warn("Permission denied on comment {} by user {} with role {}",
                        comment.getCommentId(), comment.getUserId(), userRole);
                throw new GlowGlowException(GlowGlowError.COMMENT_NO_AUTH_PERMISSION_DENIED);
            }
        }
    }
}