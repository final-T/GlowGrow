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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
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
    @GetMapping("/comments/{commentId}")
    public GlobalResponse<CommentResponseDto> getComment(@PathVariable UUID commentId) {
        CommentResponseDto comment = commentService.getComment(commentId);
        return ApiUtils.success(ResponseMessage.COMMENTS_RETRIEVE_SUCCESS.getMessage(),comment);
    }

    // 댓글 수정
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
    @DeleteMapping("/comments/{commentId}")
    public GlobalResponse<Void> deleteComment(
            @PathVariable UUID commentId,
            @AuthUser AuthUserInfo authUserInfo
    ) {
        commentService.deleteComment(commentId,authUserInfo);
        return ApiUtils.success(ResponseMessage.COMMENTS_DELETE_SUCCESS.getMessage(),null);
    }

    // 댓글 검색
    @GetMapping("/comments/search")
    public GlobalResponse<Page<CommentSearchResponseDto>> searchComment(
            Pageable pageable,
            CommentSearchCondition condition
    ){
        Page<CommentSearchResponseDto> commentResponseDtos = commentService.searchComment(pageable,condition);
        return ApiUtils.success(ResponseMessage.COMMENTS_RETRIEVE_SUCCESS.getMessage(),commentResponseDtos);
    }

}
