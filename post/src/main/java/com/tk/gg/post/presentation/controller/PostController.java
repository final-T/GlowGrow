package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.dto.PostResponseDto;
import com.tk.gg.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
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
        return ApiUtils.success("게시글이 성공적으로 생성되었습니다.",responseDto);
    }

    @GetMapping
    public  GlobalResponse<List<PostResponseDto>> getAllPosts(){
        List<PostResponseDto> responseDto = postService.getAllPosts();
        return ApiUtils.success("게시글이 성공적으로 조회되었습니다.",responseDto);
    }

    // TODO : 게시글 조회시 조회 + 카운트 증가
//    @GetMapping("/postId")
//    public GlobalResponse<PostResponseDto> getPostById(@RequestParam("postId") UUID postId){
//        PostResponseDto responseDto = postService.getPost(postId);
//        return ApiUtils.success("게시글이 성공적으로 조회되었습니다.",responseDto);
//    }

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

}
