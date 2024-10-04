package com.tk.gg.post.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostSearchCondition {
    private String title;
    private String keyword;
    private Integer minLikes;
    private Integer minViews;
    private LocalDateTime createdAt;
    //private Long userId;
}