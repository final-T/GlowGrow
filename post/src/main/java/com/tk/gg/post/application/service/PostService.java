package com.tk.gg.post.application.service;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.client.UserService;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.PostRepository;
import com.tk.gg.post.domain.service.PostDomainService;
import com.tk.gg.post.infrastructure.client.UserFeignClient;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostDomainService postDomainService;
    private final UserService userService;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, AuthUserInfo authUserInfo) {
        Post post = postDomainService.createPost(requestDto, authUserInfo);
        postRepository.save(post);
        return PostResponseDto.of(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto updatePost(UUID postId, PostRequestDto requestDto, AuthUserInfo authUserInfo) {
        Post post = getPostById(postId);

        // 사용자 존재 여부 체크
        checkUserExists(authUserInfo);

        // 권한 체크 (Provider,Customer)본인의 게시글만 수정 가능
        checkPermission(post, authUserInfo);

        postDomainService.updatePost(post, requestDto.getTitle(), requestDto.getContent(), authUserInfo);
        return PostResponseDto.of(post);
    }

    @Transactional
    public PostResponseDto.Get getPost(UUID postId) {
        Post post = getPostById(postId);
        postRepository.incrementViews(postId);
        int updatedViews = postRepository.getViews(postId);
        post.updateViews(updatedViews);
        return PostResponseDto.Get.of(post);
    }

    @Transactional
    public void deletePost(UUID postId, AuthUserInfo authUserInfo) {
        Post post = getPostById(postId);

        // 사용자 존재 여부 체크
        checkUserExists(authUserInfo);

        // 권한 체크 (Provider,Customer)본인의 게시글만 삭제 가능
        checkPermission(post, authUserInfo);

        postDomainService.softDeletePost(post,authUserInfo);
    }

    @Transactional(readOnly = true)
    public Page<PostSearchResponseDto> searchPosts(Pageable pageable, PostSearchCondition searchDto) {
        return postRepository.findPostsByCondition(searchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Post getPostById(UUID postId) {
        return postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));
    }

    @Transactional
    public boolean updatePostLikeCount(UUID postId, boolean likeStatus) {
        int updatedRows = postRepository.updateLikeCount(postId, likeStatus);
        if (updatedRows == 0) {
            throw new GlowGlowException(GlowGlowError.POST_LIKE_UPDATE_FAILED);
        }
        return true;
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
    private void checkPermission(Post post, AuthUserInfo authUserInfo) {
        UserRole userRole = authUserInfo.getUserRole();

        if (UserRole.MASTER.equals(userRole)) {
            return; // MASTER 권한은 모든 작업 허용
        }

        // 사용자 권한 확인
        if (UserRole.CUSTOMER.equals(userRole) || UserRole.PROVIDER.equals(userRole)) {
            // 게시글 작성자와 현재 사용자 ID가 다르면 권한 없음 예외 발생
            if (!post.getUserId().equals(authUserInfo.getId())) {
                log.warn("Permission denied on post {} by user {} with role {}",
                        post.getPostId(), authUserInfo.getId(), userRole);
                throw new GlowGlowException(GlowGlowError.POST_NO_AUTH_PERMISSION_DENIED);
            }
        }
    }
}