package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import com.tk.gg.post.application.service.PostService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public GlobalResponse<PostResponseDto> createPost(@AuthUser AuthUserInfo authUser, @RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.createPost(requestDto,authUser);
        return ApiUtils.success(ResponseMessage.POST_CREATE_SUCCESS.getMessage(),responseDto);
    }

    // 게시글 전체 조회
    @GetMapping
    public  GlobalResponse<List<PostResponseDto>> getAllPosts(){
        List<PostResponseDto> responseDto = postService.getAllPosts();
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public GlobalResponse<PostResponseDto.Get> getPostById(@PathVariable UUID postId){
        PostResponseDto.Get responseDto = postService.getPost(postId);
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public GlobalResponse<PostResponseDto> updatePost(
            @AuthUser AuthUserInfo authUser,
            @PathVariable UUID postId,
            @RequestBody PostRequestDto requestDto
    ) {
        PostResponseDto responseDto = postService.updatePost(postId, requestDto, authUser);
        return ApiUtils.success(ResponseMessage.POST_UPDATE_SUCCESS.getMessage(), responseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public GlobalResponse<Void> deletePost(@PathVariable UUID postId, @AuthUser AuthUserInfo authUser) {
        postService.deletePost(postId,authUser);
        return ApiUtils.success(ResponseMessage.POST_DELETE_SUCCESS.getMessage(), null);
    }

    @GetMapping("/search")
    public GlobalResponse<Page<PostSearchResponseDto>> searchPosts(
            Pageable pageable,
            PostSearchCondition searchDto
    ){
        Page<PostSearchResponseDto> postResponseDtos = postService.searchPosts(pageable,searchDto);
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),postResponseDtos);
    }

}
