package com.tk.gg.post.domain.repository;

import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.model.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostFileRepository extends JpaRepository<PostFile, UUID> {
    List<PostFile> findByPost(Post post);
}
