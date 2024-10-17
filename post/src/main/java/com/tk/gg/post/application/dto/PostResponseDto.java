package com.tk.gg.post.application.dto;

import com.tk.gg.post.domain.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private UUID postId;
    private Long userId;
    private String title;
    private String content;
    private Integer views;
    private Integer likes;

    public static PostResponseDto of(Post post) {
        return PostResponseDto.builder()
                .userId(post.getUserId())
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .likes(post.getLikes())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Get{
        private UUID postId;
        private Long userId;
        private String title;
        private String content;
        private Integer views;
        private Integer likes;
        private List<CommentResponseDto> commentList;
        private List<MultimediaDto.Response> multimediaList;

        public static Get of(Post post) {
            return Get.builder()
                    .userId(post.getUserId())
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .views(post.getViews())
                    .likes(post.getLikes())
                    .commentList(post.getComments().stream()
                            .filter(comment -> !comment.getIsDeleted())
                            .map(CommentResponseDto::of)
                            .collect(Collectors.toList()))
                    .multimediaList(post.getMultimediaList().stream()
                            .filter(multimedia -> !multimedia.getIsDeleted())
                            .map(MultimediaDto.Response::from)
                            .collect(Collectors.toList()))
                    .build();
        }


    }
}
