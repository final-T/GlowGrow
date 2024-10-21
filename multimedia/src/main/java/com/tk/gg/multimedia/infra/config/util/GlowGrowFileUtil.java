package com.tk.gg.multimedia.infra.config.util;

import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.multimedia.domain.type.FileUploadType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.tk.gg.common.response.exception.GlowGlowError.*;
import static com.tk.gg.common.response.exception.GlowGlowError.MULTIMEDIA_REQUEST_PARAM_NULL;

public class GlowGrowFileUtil {
    private final static long MAX_IMAGE_SIZE = 5 * 1024 * 1024;; // 1MB
    private final static long MAX_VIDEO_SIZE = 50 * 1024 * 1024;

    public static String validate(FileUploadType type, MultipartFile file) {
        if(type == null){
            throw new GlowGlowException(MULTIMEDIA_REQUEST_PARAM_NULL);
        }

        if(file == null){
            throw new GlowGlowException(MULTIMEDIA_REQUEST_PARAM_NULL);
        }

        fileSizeCheck(file);

        return file.getOriginalFilename();
    }

    private static void fileSizeCheck(MultipartFile file) {

        String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new GlowGlowException(MULTIMEDIA_FILE_INVALID));
        String extension = getFileExtension(fileName).toLowerCase();

        long fileSize = file.getSize();

        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                if (fileSize > MAX_IMAGE_SIZE) {
                    throw new GlowGlowException(MULTIMEDIA_FILE_SIZE_INVALID);
                }
                break;

            case "mp4":
            case "avi":
            case "mov":
                if (fileSize > MAX_VIDEO_SIZE) {
                    throw new GlowGlowException(MULTIMEDIA_FILE_SIZE_INVALID);
                }
                break;
            default:
                throw new GlowGlowException(MULTIMEDIA_FILE_EXTENSION_INVALID);
        }
    }

    // 파일 확장자 추출 메서드
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            throw new GlowGlowException(MULTIMEDIA_FILE_EXTENSION_INVALID);
        }
    }
}
