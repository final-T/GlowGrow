package com.tk.gg.post.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));
        post.updatePostBuilder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        postRepository.save(post);
        return PostResponseDto.of(post);
    }


    // TODO : 게시글 조회시 조회 + 카운트 증가
//    public PostResponseDto getPost(UUID postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));
//        return null;
//    }


    @Transactional
    public void deletePost(UUID postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_NO_EXIST));

        // JPA의 변경 감지(Dirty Checking)로 인해 명시적인 save() 호출이 필요 X
        post.softDelete();
    }

}
