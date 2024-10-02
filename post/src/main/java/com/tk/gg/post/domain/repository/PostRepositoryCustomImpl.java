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
                        post.title,
                        post.content,
                        post.createdAt,
                        post.likes,
                        post.views))
                .from(post)
                .where(
                        isNotDeleted(),
                        keywordContains(condition.getKeyword()),  // 검색 조건 적용
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

    private OrderSpecifier<?> getOrderBy(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return null; // 정렬이 없는 경우
        }

        OrderSpecifier<?> orderSpecifier = null;
        for (Sort.Order order : pageable.getSort()) {
            if (order.getProperty().equals("createdAt")) {
                orderSpecifier = order.isAscending() ? post.createdAt.asc() : post.createdAt.desc();
            }

        }
        return orderSpecifier;
    }

}
