package com.tk.gg.reservation.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.reservation.domain.model.QTimeSlot;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.repository.TimeSlotRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class TimeSlotRepositoryImpl implements TimeSlotRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QTimeSlot timeSlot = QTimeSlot.timeSlot;

    @Override
    public Page<TimeSlot> searchTimeSlots(LocalDate startDate, LocalDate endDate, Pageable pageable) {

        List<TimeSlot> timeSlotList = queryFactory
                .selectFrom(timeSlot)
                .where(
                        isDeletedByNullCondition(), // 삭제되지 않은 것만
                        startDateCondition(startDate),  // 시작 날짜 조건
                        endDateCondition(endDate)     // 종료 날짜 조건
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건
                .fetch();

        Long total = queryFactory
                .from(timeSlot)
                .select(timeSlot.count())
                .where(
                        isDeletedByNullCondition(),
                        startDateCondition(startDate),
                        endDateCondition(endDate)
                )
                .fetchOne();

        return new PageImpl<>(timeSlotList, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    if ("availableDate".equals(order.getProperty())) {
                        return order.isAscending() ? timeSlot.availableDate.asc() : timeSlot.availableDate.desc();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }

    // 시작 날짜 조건
    private BooleanExpression startDateCondition(LocalDate startDate) {
        return startDate != null ? timeSlot.availableDate.goe(startDate) : null;
    }

    // 종료 날짜 조건
    private BooleanExpression endDateCondition(LocalDate endDate) {
        return endDate != null ? timeSlot.availableDate.loe(endDate) : null;
    }

    //삭제 되지 않은 데이터
    private BooleanExpression isDeletedByNullCondition() {
        return timeSlot.deletedBy.isNull();
    }
}
