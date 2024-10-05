package com.tk.gg.post.domain.repository;

import com.tk.gg.post.application.dto.MultiMediaSearchDto;
import com.tk.gg.post.application.dto.MultimediaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MultimediaRepositoryCustom {
    Page<MultimediaDto.Response> searchMultimedia(MultiMediaSearchDto condition , Pageable pageable);
}
