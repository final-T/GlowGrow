package com.tk.gg.multimedia.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.multimedia.application.dto.MultimediaDto;
import com.tk.gg.multimedia.domain.model.Multimedia;
import com.tk.gg.multimedia.domain.repository.MultiMediaRepository;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.MULTIMEDIA_NO_EXIST;

@Service
@RequiredArgsConstructor
public class MultimediaDomainService {

    private final MultiMediaRepository multimediaRepository;

    @Transactional
    public Multimedia saveMultimedia(MultimediaDto multimediaDto) {
        return multimediaRepository.save(multimediaDto.toEntity());
    }

    @Transactional
    public void deleteMultimedia(AuthUserInfo authUserInfo, UUID multimediaId) {
        Multimedia multimedia = multimediaRepository.findByUserIdAndMultiMediaIdAndIsDeletedFalse(authUserInfo.getId(), multimediaId)
                .orElseThrow(() -> new GlowGlowException(MULTIMEDIA_NO_EXIST));

        multimedia.softDelete(authUserInfo);
    }
}
