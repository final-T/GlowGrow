package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.LikeResponseDto;
import com.tk.gg.post.application.service.LikeService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public GlobalResponse<LikeResponseDto> toggleLike(
            @PathVariable UUID postId,
            @AuthUser AuthUserInfo authUserInfo) {

        LikeResponseDto responseDto = likeService.toggleLike(postId, authUserInfo);

        ResponseMessage responseMessage = responseDto.isLikeStatus()
                ? ResponseMessage.LIKE_SUCCESS
                : ResponseMessage.UNLIKE_SUCCESS;
        return ApiUtils.success(responseMessage.getMessage(), responseDto);
    }
}
