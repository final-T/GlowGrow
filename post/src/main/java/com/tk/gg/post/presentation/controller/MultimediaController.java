package com.tk.gg.post.presentation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.post.application.dto.MultiMediaSearchDto;
import com.tk.gg.post.application.dto.MultimediaDto;
import com.tk.gg.post.application.service.MultimediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multimedia")
public class MultimediaController {

    private final MultimediaService multimediaService;

    // S3 멀티미디어 업로드 api
    @PostMapping
    public GlobalResponse<MultimediaDto.Response> uploadMultimedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("postId") UUID postId
    ){
        MultimediaDto.Response responseDto = multimediaService.uploadMultimedia(file,postId);
        return ApiUtils.success(ResponseMessage.MULTIMEDIA_UPLOAD_SUCCESS.getMessage(),responseDto);
    }

    // S3 멀티미디어 복사 api
    @GetMapping
    public ResponseEntity<InputStreamResource> getFile(@RequestParam("multiMediaId") UUID multiMediaId) {
        MultimediaDto.FileResponse responseDto = multimediaService.getFile(multiMediaId);

        // 다운로드 받는 파일의 이름은 attachment filename에 의해 결정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + responseDto.getFileName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.valueOf(responseDto.getFileType().getContentType()))
                .body(new InputStreamResource(responseDto.getFileData()));
    }

    // S3 멀티미디어 삭제 api
    @DeleteMapping
    GlobalResponse<Void> deleteMultimedia(@RequestParam("multiMediaId") UUID multiMediaId) {
        multimediaService.deleteMultimedia(multiMediaId);
        return ApiUtils.success(ResponseMessage.MULTIMEDIA_DELETE_SUCCESS.getMessage(),null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 멀티 미디어 단건 조회 api
    @GetMapping("/{multiMediaId}")
    public GlobalResponse<MultimediaDto.Response> getMultimedia(@PathVariable UUID multiMediaId){
        MultimediaDto.Response responseDto = multimediaService.getMultimedia(multiMediaId);
        return ApiUtils.success(ResponseMessage.MULTIMEDIA_RETRIEVE_SUCCESS.getMessage(),responseDto);
    }

    // 멀티 미디어 검색 api
    @GetMapping("/search")
    public GlobalResponse<Page<MultimediaDto.Response>> searchMultimedia(
            MultiMediaSearchDto condition,
            Pageable pageable
    ){
        Page<MultimediaDto.Response> responsePage = multimediaService.searchMultiMedias(condition,pageable);
        return ApiUtils.success(ResponseMessage.MULTIMEDIA_RETRIEVE_SUCCESS.getMessage(),responsePage);
    }


}
