package com.tk.gg.post.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MultiMediaSearchDto {
    private String fileName ;
    private String fileType ;
    private UUID postId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
