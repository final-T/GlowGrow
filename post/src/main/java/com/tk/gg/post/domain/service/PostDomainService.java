package com.tk.gg.post.domain.service;

import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.application.dto.PostRequestDto;
import org.springframework.stereotype.Service;

@Service
public class PostDomainService {

    public Post createPost(PostRequestDto requestDto) {
        return Post.createPostBuilder()
                .postRequestDto(requestDto)
                .build();
    }

    public void updatePost(Post post, String title, String content) {
        post.updatePostBuilder()
                .title(title)
                .content(content)
                .build();
    }

    public void incrementViews(Post post) {
        post.setViews(post.getViews() + 1);
    }

    public void softDeletePost(Post post) {
        post.softDelete();
    }

    public void updateLikeCount(Post post, boolean likeStatus) {
        if (likeStatus) {
            post.setLikes(post.getLikes() + 1);
        } else {
            if (post.getLikes() > 0) {
                post.setLikes(post.getLikes() - 1);
            }
        }
    }
}