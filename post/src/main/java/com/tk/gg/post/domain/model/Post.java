package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.post.application.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id", updatable = false, nullable = false)
    private UUID postId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "likes", nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    //소프트 삭제 메서드
    public void softDelete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        //this.deletedBy = deletedBy;
    }


    @Builder(builderClassName = "CreatePostBuilder", builderMethodName = "createPostBuilder")
    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.views = 0;
        this.likes = 0;
    }

    @Builder(builderClassName = "UpdatePostBuilder", builderMethodName = "updatePostBuilder")
    public Post update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }


}