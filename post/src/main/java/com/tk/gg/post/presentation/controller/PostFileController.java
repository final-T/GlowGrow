package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.PostFileDto;
import com.tk.gg.post.application.service.PostFileService;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
public class PostFileController {

    private final PostFileService postFileService;

    @PostMapping("/{postId}/files")
    public GlobalResponse<PostFileDto> uploadFile(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable("postId") UUID postId,
            @RequestParam("fileUrl") String fileUrl
    ) {
        PostFileDto responseDto = postFileService.uploadFile(postId, fileUrl, authUserInfo);
        return ApiUtils.success(ResponseMessage.POST_URL_CREATE_SUCCESS.getMessage(), responseDto);
    }

    @GetMapping("/{postId}/files")
    public GlobalResponse<List<PostFileDto>> getFileUrls(
            @PathVariable("postId") UUID postId,
            @AuthUser AuthUserInfo authUserInfo
    ) {
        List<PostFileDto> fileUrls = postFileService.getFileUrls(postId,authUserInfo);
        return ApiUtils.success(ResponseMessage.POST_FILE_LIST_SUCCESS.getMessage(), fileUrls);
    }

    @DeleteMapping("/{postId}/files")
    public GlobalResponse<Void> deleteFileUrl(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable("postId") UUID postId,
            @RequestParam("fileUrl") String fileUrl
    ){
        postFileService.deleteFile(postId,authUserInfo,fileUrl);
        return ApiUtils.success(ResponseMessage.POST_FILE_DELETE_SUCCESS.getMessage(), null);
    }




}
