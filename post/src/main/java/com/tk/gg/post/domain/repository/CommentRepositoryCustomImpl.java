package com.tk.gg.post.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.post.application.dto.CommentSearchCondition;
import com.tk.gg.post.application.dto.CommentSearchResponseDto;
import com.tk.gg.post.domain.model.QComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.tk.gg.post.domain.model.QComment.comment;


public class CommentRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(QComment.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<CommentSearchResponseDto> searchComments(CommentSearchCondition condition, Pageable pageable) {
        List<CommentSearchResponseDto> content = queryFactory
                .select(Projections.constructor(CommentSearchResponseDto.class,
                        comment.commentId,
                        comment.post.postId,
                        comment.userId,
                        comment.content,
                        comment.parentComment.commentId))
                .from(comment)
                .where(
                        isNotDeleted(),
                        contentContains(condition.getContent()),
                        postIdEquals(condition.getPostId()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderBy(pageable))
                .fetch();

        long total = queryFactory
                .selectFrom(comment)
                .where(
                        isNotDeleted(),
                        contentContains(condition.getContent()),
                        postIdEquals(condition.getPostId()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression isNotDeleted() {
        return comment.isDeleted.eq(false);
    }

    private BooleanExpression contentContains(String content) {
        return content != null ? comment.content.containsIgnoreCase(content) : null;
    }

    private BooleanExpression postIdEquals(UUID postId) {
        return postId != null ? comment.post.postId.eq(postId) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return comment.createdAt.between(start, end);
        } else if (start != null) {
            return comment.createdAt.goe(start);
        } else if (end != null) {
            return comment.createdAt.loe(end);
        }
        return null;
    }

    private OrderSpecifier<?> getOrderBy(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return comment.createdAt.desc(); // 기본 정렬
        }

        for (Sort.Order order : pageable.getSort()) {
            if (order.getProperty().equals("createdAt")) {
                return order.isAscending() ? comment.createdAt.asc() : comment.createdAt.desc();
            }
        }

        return comment.createdAt.desc(); // 기본 정렬
    }


}
