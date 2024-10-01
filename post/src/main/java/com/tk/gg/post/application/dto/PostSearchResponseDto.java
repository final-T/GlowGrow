package com.tk.gg.post.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PostSearchResponseDto {
    private UUID postId;
    private String title;
    private String contentPreview;
    private LocalDateTime createdAt;
    private Integer likes;
    private Integer views;

    public PostSearchResponseDto(UUID postId, String title, String content, LocalDateTime createdAt, Integer likes, Integer views) {
        this.postId = postId;
        this.title = title;
        this.contentPreview = getContentPreview(content);
        this.createdAt = createdAt;
        this.likes = likes;
        this.views = views;
    }

    private String getContentPreview(String content) {
        int maxLength = 100;
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }

}
