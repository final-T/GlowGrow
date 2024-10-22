package com.tk.gg.multimedia.application.service;

import com.tk.gg.multimedia.application.dto.MultimediaDto;
import com.tk.gg.multimedia.domain.service.MultimediaDomainService;
import com.tk.gg.multimedia.domain.service.S3Service;
import com.tk.gg.multimedia.domain.type.FileUploadType;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MultimediaService {

    private final MultimediaDomainService multimediaDomainService;
    private final S3Service s3Service;

    public MultimediaDto uploadMultimedia(AuthUserInfo authUserInfo, MultipartFile file, FileUploadType type) {
        // S3에 파일 업로드
        String uploadUrl = s3Service.uploadMultiMedia(authUserInfo.getId(), file,type);
        MultimediaDto multimediaDto = MultimediaDto.of(authUserInfo, file, type, uploadUrl);

        // 파일 정보 저장
        return MultimediaDto.from(multimediaDomainService.saveMultimedia(multimediaDto));
    }

    public void deleteMultimedia(AuthUserInfo authUserInfo, UUID multimediaId) {
        multimediaDomainService.deleteMultimedia(authUserInfo, multimediaId);
    }
}
