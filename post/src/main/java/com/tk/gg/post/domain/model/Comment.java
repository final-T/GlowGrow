package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
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
    public Comment(Post post , Comment parentComment , String content) {
        this.post = post;
        this.parentComment = parentComment;
        this.content = content;

        // 대댓글 깊이 제한 -> 댓글 - 답글
        if (parentComment != null && parentComment.getParentComment() != null) {
            throw new GlowGlowException(GlowGlowError.COMMENT_DEPTH_EXCEEDED);
        }
    }

    // userId를 설정하는 메서드
    public void assignUserId(Long userId) {
        this.userId = userId;
    }

    // content를 업데이트하는 메서드
    public void updateContent(String content) {
        this.content = content;
    }

    //소프트 삭제 메서드
    public void softDelete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        // 모든 자식 댓글에 대해 재귀적으로 softDelete 호출
        this.childComments.forEach(Comment::softDelete);
        //this.deletedBy = deletedBy;
    }

}
