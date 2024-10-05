package com.tk.gg.post.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.dto.MultiMediaSearchDto;
import com.tk.gg.post.application.dto.MultimediaDto;
import com.tk.gg.post.domain.model.Multimedia;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.MultimediaRepository;
import com.tk.gg.post.domain.service.MultimediaDomainService;
import com.tk.gg.post.domain.type.FileType;
import com.tk.gg.post.lib.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultimediaService {

    private final PostService postService;
    private final UploadService uploadService;
    private final MultimediaRepository multimediaRepository;
    private final MultimediaDomainService multimediaDomainService;

    @Value("${app.upload.max-image-size}")
    private long maxImageSize;

    @Value("${app.upload.max-video-size}")
    private long maxVideoSize;

    @Transactional
    public MultimediaDto.Response uploadMultimedia(MultipartFile file, UUID postId) {
        Post post = postService.getPostById(postId);
        String originalName = file.getOriginalFilename();
        if(originalName == null) {
            throw new GlowGlowException(GlowGlowError.INVALID_FILE_NAME);
        }

        String[] fileInfos = FileUtil.splitFileName(originalName);
        FileType fileType = FileType.findByKey(fileInfos[1])
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.UNSUPPORTED_FILE_EXTENSION));

        multimediaDomainService.checkFileSizeLimit(file.getSize(), fileType, maxImageSize, maxVideoSize);

        String uploadFileName = getFileNamePrefix() + "." + fileType.getKey();
        uploadService.uploadMultimedia(uploadFileName, file);

        Multimedia multimedia = multimediaDomainService.createMultimedia(post, uploadFileName, file.getOriginalFilename(), file.getSize(), fileType);
        multimediaRepository.save(multimedia);

        return MultimediaDto.Response.from(multimedia);
    }

    @Transactional(readOnly = true)
    public MultimediaDto.FileResponse getFile(UUID multiMediaId) {
        Multimedia multimedia = multimediaRepository.findByMultiMediaId(multiMediaId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_NO_EXIST));

        InputStream fileData = uploadService.getFile(multimedia.getMultiMediaUrl());

        return MultimediaDto.FileResponse.builder()
                .fileName(multimedia.getFileName())
                .fileType(multimedia.getFileType())
                .fileData(fileData)
                .build();
    }

    @Transactional
    public void deleteMultimedia(UUID multiMediaId) {
        Multimedia multimedia = multimediaRepository.findByMultiMediaId(multiMediaId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_NO_EXIST));

        uploadService.deleteFile(multimedia.getMultiMediaUrl());
        multimediaDomainService.softDeleteMultimedia(multimedia, 1L);
    }

    private String getFileNamePrefix() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return "GlowGrow_" + timestamp + "_" + uniqueId;
    }

    @Transactional(readOnly = true)
    public MultimediaDto.Response getMultimedia(UUID multiMediaId) {
        Multimedia multimedia = multimediaRepository.findByMultiMediaId(multiMediaId)
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_NO_EXIST));

        return MultimediaDto.Response.from(multimedia);
    }

    public Page<MultimediaDto.Response> searchMultiMedias(MultiMediaSearchDto condition, Pageable pageable) {
        Page<MultimediaDto.Response> result = multimediaRepository.searchMultimedia(condition, pageable);
        if(result.isEmpty()) {
            throw new GlowGlowException(GlowGlowError.NO_SEARCH_RESULTS);
        }
        return result;
    }
}