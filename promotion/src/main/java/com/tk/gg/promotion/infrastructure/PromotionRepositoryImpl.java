package com.tk.gg.promotion.infrastructure;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.promotion.application.dto.PromotionResponseDto;
import com.tk.gg.promotion.application.dto.PromotionSearch;
import com.tk.gg.promotion.application.dto.QPromotionResponseDto;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.PromotionStatus;
import com.tk.gg.promotion.domain.repository.PromotionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.tk.gg.promotion.domain.QPromotion.promotion;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PromotionResponseDto> findPromotionsByCondition(PromotionSearch condition, Pageable pageable) {
        List<PromotionResponseDto> promotions = queryFactory.select(
                new QPromotionResponseDto(
                        promotion.postUserId,
                        promotion.promotionId,
                        promotion.title,
                        promotion.description,
                        promotion.startDate,
                        promotion.endDate,
                        promotion.status
                ))
                .from(promotion)
                .where(
                        eqTitle(condition.getTitle()),
                        eqStatus(condition.getStatus()),
                        goeStartDate(condition.getStartDate()),
                        loeEndDate(condition.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .fetch();

        // 카운트 쿼리 지연 로딩
        JPAQuery<Promotion> count = queryFactory.selectFrom(promotion)
                .where(
                        eqTitle(condition.getTitle()),
                        eqStatus(condition.getStatus()));

        return PageableExecutionUtils.getPage(promotions, pageable, count::fetchCount);
    }


    // 정렬 조건 추가
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    if ("createdAt".equals(order.getProperty())) {
                        return order.isAscending() ? promotion.createdAt.asc() : promotion.createdAt.desc();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression eqTitle(String title) {
        return StringUtils.hasText(title) ? promotion.title.eq(title) : null;
    }

    private BooleanExpression eqStatus(PromotionStatus status) {
        return status != null ? promotion.status.eq(status) : null;
    }

    private BooleanExpression goeStartDate(LocalDate startDate) {
        return startDate != null ? promotion.startDate.goe(startDate) : null;
    }

    private BooleanExpression loeEndDate(LocalDate endDate) {
        return endDate != null ? promotion.endDate.loe(endDate) : null;
    }
}
