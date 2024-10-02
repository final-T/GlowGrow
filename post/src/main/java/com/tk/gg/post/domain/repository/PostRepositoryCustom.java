package com.tk.gg.post.domain.repository;

import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostSearchResponseDto> findPostsByCondition(PostSearchCondition searchCondition, Pageable pageable);
}
