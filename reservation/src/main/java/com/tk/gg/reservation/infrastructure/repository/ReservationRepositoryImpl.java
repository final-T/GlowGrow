package com.tk.gg.reservation.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.domain.model.QReservation;
import com.tk.gg.reservation.domain.model.Reservation;
import com.tk.gg.reservation.domain.repository.ReservationRepositoryCustom;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.security.user.AuthUserInfo;
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
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QReservation reservation = QReservation.reservation;

    @Override
    public Page<Reservation> searchReservations(
            LocalDate startDate, LocalDate endDate, ReservationStatus status, Pageable pageable, AuthUserInfo userInfo
    ) {
        List<Reservation> reservationList = queryFactory
                .selectFrom(reservation)
                .where(
                        isDeletedByNullCondition(), // 삭제되지 않은 것만
                        startDateCondition(startDate),  // 시작 날짜 조건
                        endDateCondition(endDate),     // 종료 날짜 조건
                        reservationStatusCondition(status), // 상태 조건 추가
                        userRoleCondition(userInfo)        // 사용자 권한 조건 추가
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort())) // 정렬 조건
                .fetch();

        Long total = queryFactory
                .from(reservation)
                .select(reservation.count())
                .where(
                        isDeletedByNullCondition(),
                        startDateCondition(startDate),
                        endDateCondition(endDate),
                        reservationStatusCondition(status),
                        userRoleCondition(userInfo)
                )
                .fetchOne();

        return new PageImpl<>(reservationList, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream().map(order -> {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            return switch (property) {
                case "createdAt" -> isAscending ? this.reservation.createdAt.asc() : this.reservation.createdAt.desc();
                case "updatedAt" -> isAscending ? this.reservation.updatedAt.asc() : this.reservation.updatedAt.desc();
                case "reservationDate" ->
                        isAscending ? this.reservation.reservationDate.asc() : this.reservation.reservationDate.desc();
                default -> null;
            };
        }).filter(Objects::nonNull).toArray(OrderSpecifier[]::new);
    }

    // 시작 날짜 조건
    private BooleanExpression startDateCondition(LocalDate startDate) {
        return startDate != null ? reservation.reservationDate.goe(startDate) : null;
    }

    // 종료 날짜 조건
    private BooleanExpression endDateCondition(LocalDate endDate) {
        return endDate != null ? reservation.reservationDate.loe(endDate) : null;
    }

    // ReservationStatus 조건
    private BooleanExpression reservationStatusCondition(ReservationStatus status) {
        return status != null ? reservation.reservationStatus.eq(status) : null;
    }

    //삭제 되지 않은 데이터
    private BooleanExpression isDeletedByNullCondition() {
        return reservation.deletedBy.isNull();
    }

    // 사용자 권한에 따른 조회 조건
    private BooleanExpression userRoleCondition(AuthUserInfo userInfo) {
        if (userInfo.getUserRole().equals(UserRole.MASTER)) {
            return null; // MASTER는 모든 Reservation을 조회할 수 있음
        } else if (userInfo.getUserRole().equals(UserRole.CUSTOMER)) {
            return reservation.customerId.eq(userInfo.getId()); // CUSTOMER는 자신이 고객인 예약만 조회
        } else if (userInfo.getUserRole().equals(UserRole.PROVIDER)) {
            return reservation.serviceProviderId.eq(userInfo.getId()); // PROVIDER는 자신이 제공자인 예약만 조회
        }
        return null; // 기본적으로 조회 불가
    }
}
