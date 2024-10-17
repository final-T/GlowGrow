package com.tk.gg.reservation.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.reservation.presentation.request.ReviewSearchCondition;
import com.tk.gg.reservation.domain.model.QReview;
import com.tk.gg.reservation.domain.model.Review;
import com.tk.gg.reservation.domain.repository.ReviewRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QReview review = QReview.review;

    @Override
    public Page<Review> searchReviews(ReviewSearchCondition searchCondition, Pageable pageable) {
        List<Review> reviewList = queryFactory
                .selectFrom(review)
                .where(
                        isDeletedByNullCondition(), // 삭제되지 않은 것만
                        reservationIdCondition(searchCondition.reservationId()),
                        reviewerIdCondition(searchCondition.reviewerId()),
                        targetUserIdCondition(searchCondition.targetUserId())

                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건
                .fetch();

        Long total = queryFactory
                .from(review)
                .select(review.count())
                .where(
                        isDeletedByNullCondition(),
                        reservationIdCondition(searchCondition.reservationId()),
                        reviewerIdCondition(searchCondition.reviewerId()),
                        targetUserIdCondition(searchCondition.targetUserId())
                )
                .fetchOne();

        return new PageImpl<>(reviewList, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream().map(order -> {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            return switch (property) {
                case "createdAt" -> isAscending ? this.review.createdAt.asc() : this.review.createdAt.desc();
                case "updatedAt" -> isAscending ? this.review.updatedAt.asc() : this.review.updatedAt.desc();
                default -> null;
            };
        }).filter(Objects::nonNull).toArray(OrderSpecifier[]::new);
    }

    //삭제 되지 않은 데이터
    private BooleanExpression isDeletedByNullCondition() {
        return review.deletedBy.isNull();
    }


    // 관련 예약 조건
    private BooleanExpression reservationIdCondition(UUID reservationId) {
        return reservationId != null ?
                review.reservation.id.eq(reservationId) : null;
    }

    // 해당 리뷰 작성자 조건
    private BooleanExpression reviewerIdCondition(Long reviewerId) {
        return reviewerId != null ? review.reviewerId.eq(reviewerId) : null;
    }

    // 해당 리뷰 대상자 조건
    private BooleanExpression targetUserIdCondition(Long targetUserId) {
        return targetUserId != null ? review.targetUserId.eq(targetUserId) : null;
    }
}
