package com.tk.gg.post.infrastructure;

import com.tk.gg.post.application.service.UploadService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeletedObject;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
public class S3UploadService implements UploadService {

    private final S3Client s3Client;

    private final String bucketName;

    private final String s3Path;

    public S3UploadService(
            S3Client s3Client,
            @Value("${s3.bucket-name}") String bucketName,
            @Value("${s3.path}")String s3path
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.s3Path = s3path;
    }

    @SneakyThrows
    @Override
    public void uploadMultimedia(String fileName, MultipartFile multipartFile) {
        String uploadUrl = getS3UploadUrl(fileName);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uploadUrl)
                .build();

        RequestBody content = RequestBody.fromInputStream(
                multipartFile.getInputStream(),
                multipartFile.getSize()
        );

        s3Client.putObject(request, content);
    }

    private String getS3UploadUrl(String fileName) {
        return s3Path + "/" + fileName;
    }



    @Override
    public InputStream getFile(String fileName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(getS3UploadUrl(fileName))
                .build();
        return s3Client.getObject(request);
    }

    @Override
    public void deleteFile(String fileName) {
        String fileKey = getS3UploadUrl(fileName); // key
        DeleteObjectRequest request = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        s3Client.deleteObject(request);
    }
}

