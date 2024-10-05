package com.tk.gg.post.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface UploadService {
    // 파일 업로드
    void uploadMultimedia(String fileName, MultipartFile file);

    // 파일 복사
    InputStream getFile(String fileName);

    // 퍄일 삭제
    void deleteFile(String fileName);
}
