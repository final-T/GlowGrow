package com.tk.gg.post.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tk.gg.post.domain.model.Multimedia;
import com.tk.gg.post.domain.type.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

public class MultimediaDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID multiMediaId;
        private UUID postId;
        private String multiMediaUrl;
        private String fileName;
        private Long fileSize;
        private FileType fileType;
        private LocalDateTime createTime;

        @QueryProjection
        public Response(UUID multiMediaId, String fileName, FileType fileType, Long fileSize, UUID postId, LocalDateTime createTime) {
            this.multiMediaId = multiMediaId;
            this.fileName = fileName;
            this.fileType = fileType;
            this.fileSize = fileSize;
            this.postId = postId;
            this.createTime = createTime;
        }

        public static Response from(Multimedia multimedia) {
            return Response.builder()
                    .multiMediaId(multimedia.getMultiMediaId())
                    .postId(multimedia.getPost().getPostId())
                    .multiMediaUrl(multimedia.getMultiMediaUrl())
                    .fileName(multimedia.getFileName())
                    .fileSize(multimedia.getFileSize())
                    .fileType(multimedia.getFileType())
                    .createTime(multimedia.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class FileResponse {
        private String fileName;
        private FileType fileType;
        private InputStream fileData;
    }
}