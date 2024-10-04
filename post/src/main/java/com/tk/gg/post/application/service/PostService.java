package com.tk.gg.post.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.PostRepository;
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

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = Post.createPostBuilder()
                .postRequestDto(requestDto)
                .build();

        postRepository.save(post);

        return PostResponseDto.of(post);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto updatePost(UUID postId, PostRequestDto requestDto) {
        // TODO : 본인 확인 & 권한 체크 & updatedBy
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));
        post.updatePostBuilder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        postRepository.save(post);
        return PostResponseDto.of(post);
    }


    @Transactional
    public PostResponseDto.Get getPost(UUID postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));

        // 조회수 증가
        postRepository.incrementViews(postId);

        // 현재 게시물의 조회수를 다시 조회하여 최신 조회수로 업데이트
        int updatedViews = postRepository.getViews(postId);
        post.setViews(updatedViews);

        return PostResponseDto.Get.of(post);
    }


    @Transactional
    public void deletePost(UUID postId) {
        // TODO : 본인 확인 & 권한 체크 & deletedBy
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));

        // JPA의 변경 감지(Dirty Checking)로 인해 명시적인 save() 호출이 필요 X
        post.softDelete();
    }

    public Page<PostSearchResponseDto> searchPosts(Pageable pageable, PostSearchCondition searchDto) {
        return postRepository.findPostsByCondition(searchDto,pageable);
    }

    @Transactional(readOnly = true)
    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));
    }

    @Transactional
    public Post updatePostLikeCount(UUID postId, boolean likeStatus) {
        Post post = getPostById(postId);

        int updatedRows = postRepository.updateLikeCount(postId, likeStatus);
        if (updatedRows == 0) {
            log.warn("No rows updated for post: {}. Current likes: {}, Trying to {}",
                    postId, post.getLikes(), likeStatus ? "increment" : "decrement");
            // 좋아요 수가 이미 0이고 감소하려는 경우, 또는 다른 동시성 문제로 업데이트가 실패한 경우
            // 현재 상태를 반환하고 예외를 던지지 않음
            return post;
        }
        return getPostById(postId);
    }

}
