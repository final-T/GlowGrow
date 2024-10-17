package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "like_id", updatable = false, nullable = false)
    private UUID likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "like_status", nullable = false)
    private Boolean likeStatus = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;


    @Builder
    public Like(Post post, AuthUserInfo authUserInfo) {
        this.post = post;
        this.userId = authUserInfo.getId();
        this.likeStatus = false;
        this.createdBy = String.valueOf(authUserInfo.getId());
    }

    public void toggleLikeStatus(AuthUserInfo authUserInfo) {
        this.likeStatus = !this.likeStatus;
        this.updatedBy = String.valueOf(authUserInfo.getId());
    }

    public void softDelete(AuthUserInfo authUserInfo) {
        isDeleted = true;
        deletedAt = LocalDateTime.now();
        deletedBy = String.valueOf(authUserInfo.getId());
    }

}
