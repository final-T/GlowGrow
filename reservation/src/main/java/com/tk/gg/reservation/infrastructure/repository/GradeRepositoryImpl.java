package com.tk.gg.reservation.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.reservation.application.dto.CustomerGradeDto;
import com.tk.gg.reservation.application.dto.ProviderGradeDto;
import com.tk.gg.reservation.domain.model.QGrade;
import com.tk.gg.reservation.domain.repository.GradeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class GradeRepositoryImpl implements GradeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QGrade grade = QGrade.grade;


    @Override
    public Page<CustomerGradeDto> getCustomerGradesByUserInfo(Long userId, Pageable pageable) {
        List<CustomerGradeDto> customerGrades = queryFactory
                .select(Projections.fields(
                        CustomerGradeDto.class,
                        grade.id,
                        grade.userId,
                        grade.userType,
                        grade.customerCommunication,
                        grade.customerPunctuality,
                        grade.customerManners,
                        grade.customerPaymentPromptness,
                        grade.createdAt,
                        grade.updatedAt
                ))
                .from(grade)
                .where(grade.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(grade.count())
                .from(grade)
                .where(grade.userId.eq(userId))
                .fetchOne();

        return new PageImpl<>(customerGrades, pageable, total);
    }

    @Override
    public Page<ProviderGradeDto> getProviderGradesByUserInfo(Long userId, Pageable pageable) {
        List<ProviderGradeDto> providerGrades = queryFactory
                .select(Projections.fields(
                        ProviderGradeDto.class,
                        grade.id,
                        grade.userId,
                        grade.userType,
                        grade.providerServiceQuality,
                        grade.providerProfessionalism,
                        grade.providerCommunication,
                        grade.providerPunctuality,
                        grade.createdAt,
                        grade.updatedAt
                ))
                .from(grade)
                .where(grade.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(grade.count())
                .from(grade)
                .where(grade.userId.eq(userId))
                .fetchOne();

        return new PageImpl<>(providerGrades, pageable, total);
    }



}
