package com.tk.gg.multimedia.domain.service;

import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.multimedia.infra.config.AmazonS3BucketProperties;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.MULTIMEDIA_UPLOAD_FAIL;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Operations s3Operations;
    private final AmazonS3BucketProperties s3BucketProperties;

    @SneakyThrows
    public String uploadMultiMedia(Long userId, MultipartFile file) {
        String uploadUrl = "";
        // 사용자 별 년도 월 일로 데이터 관리
        String key = userId + "/" + LocalDateTime.now().getYear() + "/" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth() + "/" + UUID.randomUUID();

        try (InputStream is = file.getInputStream()) {
           uploadUrl = s3Operations.upload(s3BucketProperties.getBucket(), key, is).getURL().toString();
        } catch (IOException e) {
            throw new GlowGlowException(MULTIMEDIA_UPLOAD_FAIL);
        }

        return uploadUrl;
    }

    public void deleteMultiMedia(String multimediaUrl) {
        String key = multimediaUrl.substring(multimediaUrl.indexOf(".com/") + 5);
        s3Operations.deleteObject(s3BucketProperties.getBucket(), key);
    }
}
