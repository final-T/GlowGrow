package com.tk.gg.post.application.service;

import com.tk.gg.post.application.dto.LikeResponseDto;
import com.tk.gg.post.domain.model.Like;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;

    @Transactional
    public LikeResponseDto toggleLike(UUID postId, Long userId) {
        Post post = postService.getPostById(postId);

        Like like = likeRepository.findFirstByPostAndUserId(post, userId)
                .orElse(new Like(post, userId));

        // 좋아요 상태 토글
        like.toggleLikeStatus();
        likeRepository.save(like);

        postService.updatePostLikeCount(postId, like.getLikeStatus());

        return new LikeResponseDto(like.getLikeId(),postId, userId, like.getLikeStatus());
    }

}
