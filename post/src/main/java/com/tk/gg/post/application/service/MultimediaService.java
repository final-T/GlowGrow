//package com.tk.gg.post.application.service;
//
//import com.tk.gg.common.enums.UserRole;
//import com.tk.gg.common.response.exception.GlowGlowError;
//import com.tk.gg.common.response.exception.GlowGlowException;
//import com.tk.gg.common.s3.FileUtil;
//import com.tk.gg.common.s3.UploadService;
//import com.tk.gg.post.application.client.UserService;
//import com.tk.gg.post.application.dto.MultiMediaSearchDto;
//import com.tk.gg.post.application.dto.MultimediaDto;
//import com.tk.gg.post.domain.model.Multimedia;
//import com.tk.gg.post.domain.model.Post;
//import com.tk.gg.post.domain.repository.MultimediaRepository;
//import com.tk.gg.post.domain.service.MultimediaDomainService;
//import com.tk.gg.post.domain.type.FileType;
//import com.tk.gg.post.infrastructure.client.UserFeignClient;
//import com.tk.gg.security.user.AuthUserInfo;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class MultimediaService {
//
//    private final PostService postService;
//    private final UploadService uploadService;
//    private final MultimediaRepository multimediaRepository;
//    private final MultimediaDomainService multimediaDomainService;
//    private final UserService userService;
//
//    @Value("${app.upload.max-image-size}")
//    private long maxImageSize;
//
//    @Value("${app.upload.max-video-size}")
//    private long maxVideoSize;
//
//    @Transactional
//    public MultimediaDto.Response uploadMultimedia(MultipartFile file, UUID postId, AuthUserInfo authUserInfo) {
//        Post post = postService.getPostById(postId);
//        String originalName = file.getOriginalFilename();
//        if(originalName == null) {
//            throw new GlowGlowException(GlowGlowError.INVALID_FILE_NAME);
//        }
//
//        String[] fileInfos = FileUtil.splitFileName(originalName);
//        FileType fileType = FileType.findByKey(fileInfos[1])
//                .orElseThrow(() -> new GlowGlowException(GlowGlowError.UNSUPPORTED_FILE_EXTENSION));
//
//        multimediaDomainService.checkFileSizeLimit(file.getSize(), fileType, maxImageSize, maxVideoSize);
//
//        String uploadFileName = getFileNamePrefix() + "." + fileType.getKey();
//        uploadService.uploadMultimedia(uploadFileName, file);
//
//        Multimedia multimedia = multimediaDomainService.createMultimedia(post, uploadFileName, file.getOriginalFilename(), file.getSize(), fileType, authUserInfo);
//        multimediaRepository.save(multimedia);
//
//        return MultimediaDto.Response.from(multimedia);
//    }
//
//    @Transactional(readOnly = true)
//    public MultimediaDto.FileResponse getFile(UUID multiMediaId) {
//        Multimedia multimedia = multimediaRepository.findByMultiMediaId(multiMediaId)
//                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_NO_EXIST));
//
//        InputStream fileData = uploadService.getFile(multimedia.getMultiMediaUrl());
//
//        return MultimediaDto.FileResponse.builder()
//                .fileName(multimedia.getFileName())
//                .fileType(multimedia.getFileType())
//                .fileData(fileData)
//                .build();
//    }
//
//    @Transactional
//    public void deleteMultimedia(UUID multiMediaId, AuthUserInfo authUserInfo) {
//        Multimedia multimedia = multimediaRepository.findByMultiMediaId(multiMediaId)
//                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_NO_EXIST));
//
//        // Feign Client 사용하여 사용자 존재 여부 확인
//        boolean userExists = userService.isUserExistsByEmail(authUserInfo.getEmail());
//        if(!userExists) {
//            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
//        }
//
//        // 권한 체크 (Provider,Customer)본인의 파일만 삭제 가능
//        checkPermission(multimedia, authUserInfo);
//
//        uploadService.deleteFile(multimedia.getMultiMediaUrl());
//        multimediaDomainService.softDeleteMultimedia(multimedia, authUserInfo);
//    }
//
//    private String getFileNamePrefix() {
//        LocalDateTime now = LocalDateTime.now();
//        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
//        return "GlowGrow_" + timestamp + "_" + uniqueId;
//    }
//
//    @Transactional(readOnly = true)
//    public MultimediaDto.Response getMultimedia(UUID multiMediaId) {
//        Multimedia multimedia = multimediaRepository.findByMultiMediaId(multiMediaId)
//                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_NO_EXIST));
//
//        return MultimediaDto.Response.from(multimedia);
//    }
//
//    public Page<MultimediaDto.Response> searchMultiMedias(MultiMediaSearchDto condition, Pageable pageable) {
//        Page<MultimediaDto.Response> result = multimediaRepository.searchMultimedia(condition, pageable);
//        if(result.isEmpty()) {
//            throw new GlowGlowException(GlowGlowError.NO_SEARCH_RESULTS);
//        }
//        return result;
//    }
//
//    // 권한 체크 메서드
//    private void checkPermission(Multimedia multimedia, AuthUserInfo authUserInfo) {
//        UserRole userRole = authUserInfo.getUserRole();
//
//        if (UserRole.MASTER.equals(userRole)) {
//            return; // MASTER 권한은 모든 작업 허용
//        }
//
//        // 사용자 권한 확인
//        if (UserRole.CUSTOMER.equals(userRole) || UserRole.PROVIDER.equals(userRole)) {
//            // 멀티미디어 ID와 현재 사용자 ID가 다르면 권한 없음 예외 발생
//            if (!multimedia.getUserId().equals(authUserInfo.getId())) {
//                log.warn("Permission denied on multimedia {} by user {} with role {}",
//                        multimedia.getMultiMediaId(), authUserInfo.getId(), userRole);
//                throw new GlowGlowException(GlowGlowError.POST_NO_AUTH_PERMISSION_DENIED);
//            }
//        }
//    }
//
//
//}