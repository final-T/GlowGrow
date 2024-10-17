package com.tk.gg.post.domain.service;

import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.security.user.AuthUserInfo;
import org.springframework.stereotype.Service;

@Service
public class PostDomainService {

    public Post createPost(PostRequestDto requestDto, AuthUserInfo authUserInfo) {
        return Post.createPostBuilder()
                .postRequestDto(requestDto)
                .authUserInfo(authUserInfo)
                .build();
    }

    public void updatePost(Post post, String title, String content, AuthUserInfo authUserInfo) {
        post.updatePostBuilder()
                .authUserInfo(authUserInfo)
                .title(title)
                .content(content)
                .build();
    }

    public void softDeletePost(Post post, AuthUserInfo authUserInfo) {
        post.softDelete(authUserInfo);
    }

}