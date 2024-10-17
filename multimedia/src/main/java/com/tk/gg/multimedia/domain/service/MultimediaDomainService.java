package com.tk.gg.multimedia.domain.service;

import com.tk.gg.multimedia.application.dto.MultimediaDto;
import com.tk.gg.multimedia.domain.repository.MultiMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MultimediaDomainService {

    private final MultiMediaRepository multimediaRepository;

    @Transactional
    public void saveMultimedia(MultimediaDto multimediaDto) {
        multimediaRepository.save(multimediaDto.toEntity());
    }
}
