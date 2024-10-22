package com.tk.gg.post.domain.service;

import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.model.PostFile;
import com.tk.gg.post.domain.repository.PostFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFileDomainService {
    private final PostFileRepository postFileRepository;

    @Transactional
    public PostFile createPostFile(Post post, String fileUrl) {
        PostFile postFile = PostFile.builder()
                .post(post)
                .fileUrl(fileUrl)
                .build();

        return postFileRepository.save(postFile);
    }

    public List<PostFile> getPostFiles(Post post) {
        return postFileRepository.findByPost(post);
    }

    @Transactional
    public void deletePostFile(PostFile postFile) {
        postFile.softDelete();
        postFileRepository.save(postFile);
    }
}