package com.tk.gg.post.domain.repository;

import com.tk.gg.post.application.dto.CommentSearchCondition;
import com.tk.gg.post.application.dto.CommentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<CommentSearchResponseDto> searchComments(CommentSearchCondition condition, Pageable pageable);
}
