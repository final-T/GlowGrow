package com.tk.gg.post.domain.service;


import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.post.domain.model.Comment;
import com.tk.gg.post.domain.model.Post;
import org.springframework.stereotype.Service;

@Service
public class CommentDomainService {

    public Comment createComment(Post post, Comment parentComment, String content, Long userId) {
        Comment comment = Comment.CreateCommentBuilder()
                .post(post)
                .parentComment(parentComment)
                .content(content)
                .build();

        comment.assignUserId(userId);
        return comment;
    }

    public void validateParentComment(Comment parentComment) {
        if (parentComment.getIsDeleted()) {
            throw new GlowGlowException(GlowGlowError.COMMENT_DELETED);
        }

        if (parentComment.getParentComment() != null) {
            throw new GlowGlowException(GlowGlowError.COMMENT_DEPTH_EXCEEDED);
        }
    }

    public void updateCommentContent(Comment comment, String content) {
        comment.updateContent(content);
    }

    public void softDeleteComment(Comment comment) {
        comment.softDelete();
    }
}
