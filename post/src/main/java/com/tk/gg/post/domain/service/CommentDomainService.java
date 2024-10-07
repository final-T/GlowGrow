package com.tk.gg.post.domain.service;


import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.domain.model.Comment;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.security.user.AuthUserInfo;
import org.springframework.stereotype.Service;

@Service
public class CommentDomainService {

    public Comment createComment(Post post, Comment parentComment, String content, AuthUserInfo authUserInfo) {
        return Comment.CreateCommentBuilder()
                .post(post)
                .authUserInfo(authUserInfo)
                .parentComment(parentComment)
                .content(content)
                .build();
    }

    public void validateParentComment(Comment parentComment) {
        if (parentComment.getIsDeleted()) {
            throw new GlowGlowException(GlowGlowError.COMMENT_DELETED);
        }

        if (parentComment.getParentComment() != null) {
            throw new GlowGlowException(GlowGlowError.COMMENT_DEPTH_EXCEEDED);
        }
    }

    public void updateCommentContent(Comment comment, String content, AuthUserInfo authUserInfo) {
        comment.updateContent(content, authUserInfo);
    }

    public void softDeleteComment(Comment comment, AuthUserInfo authUserInfo) {
        comment.softDelete(authUserInfo);
    }
}
