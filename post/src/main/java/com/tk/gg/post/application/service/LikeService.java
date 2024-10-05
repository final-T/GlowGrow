package com.tk.gg.post.application.service;

import com.tk.gg.post.application.dto.LikeResponseDto;
import com.tk.gg.post.domain.model.Like;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.LikeRepository;
import com.tk.gg.post.domain.service.LikeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final LikeDomainService likeDomainService;

    @Transactional
    public LikeResponseDto toggleLike(UUID postId, Long userId) {
        Post post = postService.getPostById(postId);

        Like like = likeRepository.findFirstByPostAndUserId(post, userId)
                .orElseGet(() -> likeDomainService.createLike(post, userId));

        likeDomainService.toggleLikeStatus(like);
        likeRepository.save(like);

        boolean likeStatus = likeDomainService.getLikeStatus(like);
        postService.updatePostLikeCount(postId, likeStatus);

        return new LikeResponseDto(like.getLikeId(), postId, userId, likeStatus);
    }
}