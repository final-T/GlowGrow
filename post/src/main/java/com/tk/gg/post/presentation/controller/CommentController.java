package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.CommentRequestDto;
import com.tk.gg.post.application.dto.CommentResponseDto;
import com.tk.gg.post.application.dto.CommentSearchCondition;
import com.tk.gg.post.application.dto.CommentSearchResponseDto;
import com.tk.gg.post.application.service.CommentService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "게시글 댓글" +
        " API")
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @Operation(summary = "게시글 댓글 생성 API", description = "게시글에 댓글을 작성하는 API 입니다. (댓글-답글 형태만 가능합니다.)")
    @PostMapping("/{postId}/comments")
    public GlobalResponse<CommentResponseDto> writeComment(
            @PathVariable UUID postId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthUser AuthUserInfo authUserInfo
            ) {
        CommentResponseDto responseDto = commentService.writeComment(postId,commentRequestDto,authUserInfo);
        return ApiUtils.success(ResponseMessage.COMMENTS_WRITE_SUCCESS.getMessage(),responseDto);
    }

    // 댓글 조회
    @Operation(summary = "특정 게시글 댓글 조회 API", description = "게시글의 특정 댓글을 조회하는 API 입니다.")
    @GetMapping("/comments/{commentId}")
    public GlobalResponse<CommentResponseDto> getComment(@PathVariable UUID commentId) {
        CommentResponseDto comment = commentService.getComment(commentId);
        return ApiUtils.success(ResponseMessage.COMMENTS_RETRIEVE_SUCCESS.getMessage(),comment);
    }

    // 댓글 수정
    @Operation(summary = "게시글 댓글 수정 API", description = "게시글을 특정 댓글을 수정하는 API 입니다.")
    @PatchMapping("/comments/{commentId}")
    public GlobalResponse<CommentResponseDto> updateComment(
            @PathVariable UUID commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthUser AuthUserInfo authUserInfo
    ) {
        CommentResponseDto updateComment = commentService.updateComment(commentId,commentRequestDto,authUserInfo);
        return ApiUtils.success(ResponseMessage.COMMENTS_UPDATE_SUCCESS.getMessage(),updateComment);
    }

    // 댓글 삭제
    @Operation(summary = "게시글 댓글 삭제 API", description = "게시글의 특정 댓글을 삭제하는 API 입니다.")
    @DeleteMapping("/comments/{commentId}")
    public GlobalResponse<Void> deleteComment(
            @PathVariable UUID commentId,
            @AuthUser AuthUserInfo authUserInfo
    ) {
        commentService.deleteComment(commentId,authUserInfo);
        return ApiUtils.success(ResponseMessage.COMMENTS_DELETE_SUCCESS.getMessage(),null);
    }

    // 댓글 검색
    @Operation(summary = "게시글 댓글 검색 API", description = "게시글의 댓글을 검색하는 API 입니다.")
    @GetMapping("/comments/search")
    public GlobalResponse<Page<CommentSearchResponseDto>> searchComment(
            Pageable pageable,
            CommentSearchCondition condition
    ){
        Page<CommentSearchResponseDto> commentResponseDtos = commentService.searchComment(pageable,condition);
        return ApiUtils.success(ResponseMessage.COMMENTS_RETRIEVE_SUCCESS.getMessage(),commentResponseDtos);
    }

}
