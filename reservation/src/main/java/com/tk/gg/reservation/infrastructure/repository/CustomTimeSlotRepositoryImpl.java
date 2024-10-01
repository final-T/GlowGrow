package com.tk.gg.reservation.infrastructure.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.reservation.domain.model.QTimeSlot;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.repository.CustomTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomTimeSlotRepositoryImpl implements CustomTimeSlotRepository {
    private final JPAQueryFactory queryFactory;
    QTimeSlot timeSlot = QTimeSlot.timeSlot;

    @Override
    public Page<TimeSlot> searchTimeSlots(Date startDate, Date endDate, Pageable pageable) {

        List<TimeSlot> timeSlotList = queryFactory
                .selectFrom(timeSlot)
                .where(
                        isDeletedByNotNullCondition(), // 삭제되지 않은 것만
                        startDateCondition(startDate),  // 시작 날짜 조건
                        endDateCondition(endDate)     // 종료 날짜 조건
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(timeSlot.availableDate.desc()) // 정렬 조건
                .fetch();

        Long total = queryFactory
                .from(timeSlot)
                .select(timeSlot.count())
                .where(
                        isDeletedByNotNullCondition(), 
                        startDateCondition(startDate),
                        endDateCondition(endDate)
                )
                .fetchOne();

        return new PageImpl<>(timeSlotList, pageable, total);
    }

    // 시작 날짜 조건
    private BooleanExpression startDateCondition(Date startDate) {
        return startDate != null ? timeSlot.availableDate.goe(startDate) : null;
    }

    // 종료 날짜 조건
    private BooleanExpression endDateCondition(Date endDate) {
        return endDate != null ? timeSlot.availableDate.loe(endDate) : null;
    }

    //삭제 되지 않은 데이터
    private BooleanExpression isDeletedByNotNullCondition() {
        return timeSlot.deletedBy.isNotNull();
    }
}
