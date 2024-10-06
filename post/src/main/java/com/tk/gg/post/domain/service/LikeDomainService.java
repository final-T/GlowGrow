package com.tk.gg.post.domain.service;

import com.tk.gg.post.domain.model.Like;
import com.tk.gg.post.domain.model.Post;
import org.springframework.stereotype.Service;

@Service
public class LikeDomainService {

    public Like createLike(Post post, Long userId) {
        return new Like(post, userId);
    }

    public void toggleLikeStatus(Like like) {
        like.toggleLikeStatus();
    }

    public boolean getLikeStatus(Like like) {
        return like.getLikeStatus();
    }
}