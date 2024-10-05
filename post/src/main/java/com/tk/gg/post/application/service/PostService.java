package com.tk.gg.post.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.PostRepository;
import com.tk.gg.post.domain.service.PostDomainService;
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

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = postDomainService.createPost(requestDto);
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
    public PostResponseDto updatePost(UUID postId, PostRequestDto requestDto) {
        Post post = getPostById(postId);
        postDomainService.updatePost(post, requestDto.getTitle(), requestDto.getContent());
        return PostResponseDto.of(post);
    }

    @Transactional
    public PostResponseDto.Get getPost(UUID postId) {
        Post post = getPostById(postId);
        postDomainService.incrementViews(post);
        postRepository.save(post);
        return PostResponseDto.Get.of(post);
    }

    @Transactional
    public void deletePost(UUID postId) {
        Post post = getPostById(postId);
        postDomainService.softDeletePost(post);
    }

    @Transactional(readOnly = true)
    public Page<PostSearchResponseDto> searchPosts(Pageable pageable, PostSearchCondition searchDto) {
        return postRepository.findPostsByCondition(searchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));
    }

    @Transactional
    public Post updatePostLikeCount(UUID postId, boolean likeStatus) {
        Post post = getPostById(postId);
        postDomainService.updateLikeCount(post, likeStatus);
        return postRepository.save(post);
    }

    private Long getCurrentUserId() {
        return 1L; // 임시 값, 실제 구현에서는 인증된 사용자의 ID를 반환해야 함
    }
}