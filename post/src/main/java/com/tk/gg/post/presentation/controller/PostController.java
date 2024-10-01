package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import com.tk.gg.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public GlobalResponse<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.createPost(requestDto);
        return ApiUtils.success(ResponseMessage.POST_CREATE_SUCCESS.getMessage(),responseDto);
    }

    @GetMapping
    public  GlobalResponse<List<PostResponseDto>> getAllPosts(){
        List<PostResponseDto> responseDto = postService.getAllPosts();
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    @GetMapping("/{postId}")
    public GlobalResponse<PostResponseDto> getPostById(@PathVariable UUID postId){
        PostResponseDto responseDto = postService.getPost(postId);
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    @PatchMapping("/{postId}")
    public GlobalResponse<PostResponseDto> updatePost(@PathVariable UUID postId, @RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.updatePost(postId, requestDto);
        return ApiUtils.success(ResponseMessage.POST_UPDATE_SUCCESS.getMessage(), responseDto);
    }

    @DeleteMapping("/{postId}")
    public GlobalResponse<Void> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
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
