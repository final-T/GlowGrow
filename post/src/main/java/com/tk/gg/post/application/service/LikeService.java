package com.tk.gg.post.application.service;

import com.tk.gg.post.application.dto.LikeResponseDto;
import com.tk.gg.post.domain.model.Like;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.LikeRepository;
import com.tk.gg.post.domain.service.LikeDomainService;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final LikeDomainService likeDomainService;

    @Transactional
    public LikeResponseDto toggleLike(UUID postId, AuthUserInfo authUserInfo) {
        Post post = postService.getPostById(postId);

        Like like = likeRepository.findFirstByPostAndUserId(post, authUserInfo.getId())
                .orElseGet(() -> likeDomainService.createLike(post, authUserInfo));

        boolean newLikeStatus = !likeDomainService.getLikeStatus(like);

        // 좋아요 증가
        boolean updateSuccess = postService.updatePostLikeCount(postId, newLikeStatus);

        if (updateSuccess) {
            likeDomainService.toggleLikeStatus(like,authUserInfo);
            likeRepository.save(like);
        } else {
            newLikeStatus = likeDomainService.getLikeStatus(like);
        }

        return new LikeResponseDto(like.getLikeId(), postId, like.getUserId(), newLikeStatus);
    }
}