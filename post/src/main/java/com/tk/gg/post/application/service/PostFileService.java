package com.tk.gg.post.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.application.dto.PostFileDto;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.model.PostFile;
import com.tk.gg.post.domain.service.PostFileDomainService;
import com.tk.gg.security.user.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostFileService {
    private final PostFileDomainService postFileDomainService;
    private final PostService postService;

    public PostFileDto uploadFile(UUID postId, String fileUrl, AuthUserInfo authUserInfo) {
        Post post = postService.getPostById(postId);

        validatePostOwner(post, authUserInfo);

        PostFile postFile = postFileDomainService.createPostFile(post, fileUrl);
        return PostFileDto.fromEntity(postFile);
    }

    public List<PostFileDto> getFileUrls(UUID postId, AuthUserInfo authUserInfo) {
        Post post = postService.getPostById(postId);

        validatePostOwner(post, authUserInfo);

        List<PostFile> postFiles = postFileDomainService.getPostFiles(post);
        return postFiles.stream()
                .filter(f -> !f.getIsDeleted())
                .map(PostFileDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteFile(UUID postId, AuthUserInfo authUserInfo, String fileUrl) {
        Post post = postService.getPostById(postId);

        validatePostOwner(post, authUserInfo);

        PostFile postFile = postFileDomainService.getPostFiles(post).stream()
                .filter(f -> f.getFileUrl().equals(fileUrl))
                .findFirst()
                .orElseThrow(() -> new GlowGlowException(GlowGlowError.POST_FILE_NO_EXIST));
        postFileDomainService.deletePostFile(postFile);
    }


    private void validatePostOwner(Post post, AuthUserInfo authUserInfo) {
        if (!post.getUserId().equals(authUserInfo.getId())) {
            throw new GlowGlowException(GlowGlowError.POST_NO_AUTH_PERMISSION_DENIED);
        }
    }

}