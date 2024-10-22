package com.tk.gg.post.application.dto;

import com.tk.gg.post.domain.model.PostFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostFileDto {
    private UUID id;          // PostFile ID
    private String fileUrl;   // 파일 URL
    private UUID postId; // 게시물 ID
    private Boolean isDeleted; // 삭제 여부


    // PostFile 엔티티로부터 DTO를 생성하는 정적 메서드
    public static PostFileDto fromEntity(PostFile postFile) {
        return PostFileDto.builder()
                .id(postFile.getId())
                .fileUrl(postFile.getFileUrl())
                .postId(postFile.getPost().getPostId())
                .isDeleted(postFile.getIsDeleted())
                .build();
    }

}
