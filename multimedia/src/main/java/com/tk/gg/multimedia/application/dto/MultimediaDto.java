package com.tk.gg.multimedia.application.dto;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.multimedia.domain.model.Multimedia;
import com.tk.gg.multimedia.domain.type.FileType;
import com.tk.gg.multimedia.domain.type.FileUploadType;
import com.tk.gg.multimedia.infra.config.util.GlowGrowFileUtil;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MultimediaDto(
        UUID multiMediaId,
        FileUploadType fileUploadType,
        Long userId,
        String multiMediaUrl,
        String fileName,
        Long fileSize,
        FileType fileType,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static MultimediaDto of(AuthUserInfo authUserInfo, MultipartFile file, FileUploadType type, String uploadUrl) {
        String originalFileName = GlowGrowFileUtil.validate(type, file);
        Long fileSize = file.getSize();
        FileType fileType = FileType.findByKey(GlowGrowFileUtil.getFileExtension(originalFileName))
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.MULTIMEDIA_FILE_EXTENSION_INVALID));

        return MultimediaDto.builder()
                .fileUploadType(type)
                .userId(authUserInfo.getId())
                .multiMediaUrl(uploadUrl)
                .fileName(originalFileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .build();
    }

    public static MultimediaDto from(Multimedia entity) {
        return MultimediaDto.builder()
                .multiMediaId(entity.getMultiMediaId())
                .fileUploadType(entity.getUploadType())
                .userId(entity.getUserId())
                .multiMediaUrl(entity.getMultiMediaUrl())
                .fileName(entity.getFileName())
                .fileSize(entity.getFileSize())
                .fileType(entity.getFileType())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }

    public Multimedia toEntity() {
        return Multimedia.create(fileUploadType, userId, multiMediaUrl, fileName, fileSize, fileType);
    }
}
