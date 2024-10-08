package com.tk.gg.post.domain.service;

import com.tk.gg.post.domain.model.Like;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.security.user.AuthUserInfo;
import org.springframework.stereotype.Service;

@Service
public class LikeDomainService {

    public Like createLike(Post post, AuthUserInfo authUserInfo) {
        return new Like(post, authUserInfo);
    }

    public void toggleLikeStatus(Like like, AuthUserInfo authUserInfo) {
        like.toggleLikeStatus(authUserInfo);
    }

    public boolean getLikeStatus(Like like) {
        return like.getLikeStatus();
    }

}