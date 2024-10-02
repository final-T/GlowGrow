package com.tk.gg.post.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private UUID likeId;
    private UUID postId;
    private Long userId;
    private boolean likeStatus;
}
