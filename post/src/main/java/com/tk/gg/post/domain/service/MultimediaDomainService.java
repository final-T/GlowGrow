package com.tk.gg.post.domain.service;


import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.domain.model.Multimedia;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.type.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MultimediaDomainService {

    public Multimedia createMultimedia(Post post, String multiMediaUrl, String fileName, Long fileSize, FileType fileType) {
        return Multimedia.createMultimediaBuilder()
                .post(post)
                .multiMediaUrl(multiMediaUrl)
                .fileName(fileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .build();
    }

    public void softDeleteMultimedia(Multimedia multimedia, Long userId) {
        multimedia.softDelete(userId);
    }

    public void checkFileSizeLimit(long fileSize, FileType fileType, long maxImageSize, long maxVideoSize) {
        if (fileType.isImage() && fileSize > maxImageSize) {
            throw new GlowGlowException(GlowGlowError.IMAGE_FILE_SIZE_EXCEEDED);
        } else if (fileType.isVideo() && fileSize > maxVideoSize) {
            throw new GlowGlowException(GlowGlowError.VIDEO_FILE_SIZE_EXCEEDED);
        }
    }
}