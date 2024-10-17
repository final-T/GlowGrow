package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private UUID commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;


    @Builder(builderClassName = "CreateCommentBuilder", builderMethodName = "CreateCommentBuilder")
    public Comment(Post post , Comment parentComment , String content, AuthUserInfo authUserInfo) {
        this.post = post;
        this.parentComment = parentComment;
        this.content = content;
        this.createdBy = String.valueOf(authUserInfo.getId());
        this.userId = authUserInfo.getId();

        // 대댓글 깊이 제한 -> 댓글 - 답글
        if (parentComment != null && parentComment.getParentComment() != null) {
            throw new GlowGlowException(GlowGlowError.COMMENT_DEPTH_EXCEEDED);
        }
    }

    // content를 업데이트하는 메서드
    public void updateContent(String content, AuthUserInfo authUserInfo) {
        this.content = content;
        this.updatedBy = String.valueOf(authUserInfo.getId());
    }

    //소프트 삭제 메서드
    public void softDelete(AuthUserInfo authUserInfo){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = String.valueOf(authUserInfo.getId());
        // 모든 자식 댓글(답글)에 대해 재귀적으로 softDelete 호출
        this.childComments.forEach(comment -> comment.softDelete(authUserInfo));
    }

}
