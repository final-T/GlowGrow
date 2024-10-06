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

    public void softDeletePost(Post post) {
        post.softDelete();
    }

}