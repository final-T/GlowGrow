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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    public PostResponseDto getPost(UUID postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));

        // 조회수 증가
        postRepository.incrementViews(postId);

        // 현재 게시물의 조회수를 다시 조회하여 최신 조회수로 업데이트
        int updatedViews = postRepository.getViews(postId);
        post.setViews(updatedViews);

        return PostResponseDto.of(post);
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
}
