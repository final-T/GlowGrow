package com.tk.gg.post.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentSearchCondition {
    private String content;
    private UUID postId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
