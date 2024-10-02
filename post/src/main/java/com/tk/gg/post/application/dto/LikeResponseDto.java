package com.tk.gg.post.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private UUID postId;
    private Long userId;
    private boolean likeStatus;
    private int likeCount;
}
