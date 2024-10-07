package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Setter
    @Column(name = "views", nullable = false)
    private Integer views;

    @Setter
    @Column(name = "likes", nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Multimedia> multimediaList = new ArrayList<>();


    //소프트 삭제 메서드
    public void softDelete(AuthUserInfo authUserInfo){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = String.valueOf(authUserInfo.getId());

        // 모든 댓글에 대해 소프트 삭제 수행
        this.comments.forEach(comment -> comment.softDelete(authUserInfo));

        // 좋아요에 대해 소프트 삭제 수행
        this.likesList.forEach(like -> like.softDelete(authUserInfo));

        // 멀티미디어에 대해 소프트 삭제 수행
        this.multimediaList.forEach(multimedia -> multimedia.softDelete(authUserInfo));
    }


    @Builder(builderClassName = "CreatePostBuilder", builderMethodName = "createPostBuilder")
    public Post(PostRequestDto postRequestDto , AuthUserInfo authUserInfo) {
        this.userId = authUserInfo.getId();
        this.createdBy = String.valueOf(authUserInfo.getId());
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.views = 0;
        this.likes = 0;
    }

    @Builder(builderClassName = "UpdatePostBuilder", builderMethodName = "updatePostBuilder")
    public Post update(String title, String content, AuthUserInfo authUserInfo) {
        this.title = title;
        this.content = content;
        this.updatedBy = String.valueOf(authUserInfo.getId());
        return this;
    }


}