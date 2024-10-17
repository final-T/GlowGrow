package com.tk.gg.multimedia.presenation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.multimedia.application.service.MultimediaService;
import com.tk.gg.multimedia.domain.type.FileUploadType;
import com.tk.gg.multimedia.presenation.response.UploadResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.MULTIMEDIA_DELETE_SUCCESS;
import static com.tk.gg.common.response.ResponseMessage.MULTIMEDIA_UPLOAD_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multimedia")
public class MultimediaController {

    private final MultimediaService multimediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GlobalResponse<UploadResponse> uploadMultimedia(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestPart("file") MultipartFile file,
            @RequestParam("type") FileUploadType type
    ) {
        return ApiUtils.success(MULTIMEDIA_UPLOAD_SUCCESS.getMessage(), UploadResponse.from(multimediaService.uploadMultimedia(authUserInfo, file, type)));
    }

    @DeleteMapping("/{multimediaId}")
    public GlobalResponse<String> deleteMultimedia(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable("multimediaId") UUID multimediaId) {
        multimediaService.deleteMultimedia(authUserInfo, multimediaId);
        return ApiUtils.success(MULTIMEDIA_DELETE_SUCCESS.getMessage());
    }
}
