package com.tk.gg.post.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.post.application.dto.PostSearchCondition;
import com.tk.gg.post.application.dto.PostSearchResponseDto;
import com.tk.gg.post.domain.model.QPost;
import com.tk.gg.post.domain.repository.PostRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.tk.gg.post.domain.model.QPost.post;

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(QPost.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PostSearchResponseDto> findPostsByCondition(PostSearchCondition condition, Pageable pageable) {
        QPost post = QPost.post;

        List<PostSearchResponseDto> content = queryFactory
                .select(Projections.constructor(PostSearchResponseDto.class,
                        post.postId,
                        post.userId,
                        post.title,
                        post.content,
                        post.createdAt,
                        post.likes,
                        post.views))
                .from(post)
                .where(
                        isNotDeleted(),
                        keywordContains(condition.getKeyword()),  // 게시물 내용
                        titleContains(condition.getTitle()),
                        likesGreaterThanOrEqual(condition.getMinLikes()),
                        viewsGreaterThanOrEqual(condition.getMinViews())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderBy(pageable))
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .where(
                        isNotDeleted(),
                        keywordContains(condition.getKeyword()),
                        likesGreaterThanOrEqual(condition.getMinLikes()),
                        viewsGreaterThanOrEqual(condition.getMinViews())
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    // 조건 메서드들
    private BooleanExpression isNotDeleted() {
        return post.isDeleted.eq(false);
    }

    private BooleanExpression keywordContains(String keyword) {
        return keyword != null ?
                post.title.containsIgnoreCase(keyword)
                        .or(post.content.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression likesGreaterThanOrEqual(Integer minLikes) {
        return minLikes != null ? post.likes.goe(minLikes) : null;
    }

    private BooleanExpression viewsGreaterThanOrEqual(Integer minViews) {
        return minViews != null ? post.views.goe(minViews) : null;
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? post.title.containsIgnoreCase(title) : null;
    }

    private OrderSpecifier<?> getOrderBy(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return post.createdAt.desc(); // 기본 정렬: 생성일 내림차순
        }

        for (Sort.Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "createdAt":
                    return order.isAscending() ? post.createdAt.asc() : post.createdAt.desc();
                case "likes":
                    return order.isAscending() ? post.likes.asc() : post.likes.desc();
                case "views":
                    return order.isAscending() ? post.views.asc() : post.views.desc();
                // 다른 정렬 기준들...
            }
        }

        return post.createdAt.desc(); // 기본 정렬

    }
}
