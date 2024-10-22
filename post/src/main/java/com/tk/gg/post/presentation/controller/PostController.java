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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Post API", description = "게시글 API")
@Slf4j
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @Operation(summary = "게시글 생성 API", description = "게시글을 생성하는 API 입니다.")
    @PostMapping
    public GlobalResponse<PostResponseDto> createPost(@AuthUser AuthUserInfo authUser, @RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.createPost(requestDto,authUser);
        return ApiUtils.success(ResponseMessage.POST_CREATE_SUCCESS.getMessage(),responseDto);
    }

    // 게시글 전체 조회
    @Operation(summary = "게시글 전체 조회 API", description = "전체 게시글을 조회하는 API 입니다.")
    @GetMapping
    public  GlobalResponse<List<PostResponseDto>> getAllPosts(){
        List<PostResponseDto> responseDto = postService.getAllPosts();
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    // 게시글 조회
    @Operation(summary = "게시글 단건 조회 API", description = "특정 게시글을 조회하는 API 입니다.")
    @GetMapping("/{postId}")
    public GlobalResponse<PostResponseDto.Get> getPostById(@PathVariable UUID postId){
        PostResponseDto.Get responseDto = postService.getPost(postId);
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정 API", description = "특정 게시글을 수정하는 API 입니다.")
    public GlobalResponse<PostResponseDto> updatePost(
            @AuthUser AuthUserInfo authUser,
            @PathVariable UUID postId,
            @RequestBody PostRequestDto requestDto
    ) {
        PostResponseDto responseDto = postService.updatePost(postId, requestDto, authUser);
        return ApiUtils.success(ResponseMessage.POST_UPDATE_SUCCESS.getMessage(), responseDto);
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제하는 API 입니다. (해당 게시글에 있는 댓글, 좋아요도 모두 삭제됩니다.)")
    @DeleteMapping("/{postId}")
    public GlobalResponse<Void> deletePost(@PathVariable UUID postId, @AuthUser AuthUserInfo authUser) {
        postService.deletePost(postId,authUser);
        return ApiUtils.success(ResponseMessage.POST_DELETE_SUCCESS.getMessage(), null);
    }

    @Operation(summary = "게시글 검색 API", description = "게시글을 검색하는 API 입니다. (title, keyword, minLikes, minViews, createdAt)")
    @GetMapping("/search")
    public GlobalResponse<Page<PostSearchResponseDto>> searchPosts(
            Pageable pageable,
            PostSearchCondition searchDto
    ){
        Page<PostSearchResponseDto> postResponseDtos = postService.searchPosts(pageable,searchDto);
        return ApiUtils.success(ResponseMessage.POST_RETRIEVE_SUCCESS.getMessage(),postResponseDtos);
    }

}
