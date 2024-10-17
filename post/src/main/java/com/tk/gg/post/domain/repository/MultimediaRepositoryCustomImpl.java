package com.tk.gg.post.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.post.application.dto.MultiMediaSearchDto;
import com.tk.gg.post.application.dto.MultimediaDto;
import com.tk.gg.post.application.dto.QMultimediaDto_Response;
import com.tk.gg.post.domain.model.QMultimedia;
import com.tk.gg.post.domain.type.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.tk.gg.post.domain.model.QMultimedia.multimedia;

public class MultimediaRepositoryCustomImpl extends QuerydslRepositorySupport implements MultimediaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MultimediaRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(QMultimedia.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<MultimediaDto.Response> searchMultimedia(MultiMediaSearchDto condition, Pageable pageable) {
        List<MultimediaDto.Response> content = queryFactory
                .select(new QMultimediaDto_Response(
                        multimedia.multiMediaId,
                        multimedia.fileName,
                        multimedia.fileType,
                        multimedia.fileSize,
                        multimedia.post.postId,
                        multimedia.createdAt
                        ))
                .from(multimedia)
                .where(
                        isNotDeleted(),
                        fileNameContains(condition.getFileName()),
                        fileTypeEquals(condition.getFileType()),
                        postIdEquals(condition.getPostId()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderBy(pageable))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(multimedia.count())
                .from(multimedia)
                .where(
                        isNotDeleted(),
                        fileNameContains(condition.getFileName()),
                        fileTypeEquals(condition.getFileType()),
                        postIdEquals(condition.getPostId()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isNotDeleted() {
        return multimedia.isDeleted.eq(false);
    }

    private BooleanExpression fileNameContains(String fileName) {
        return fileName != null ? multimedia.fileName.containsIgnoreCase(fileName) : null;
    }

    private BooleanExpression fileTypeEquals(String fileType) {
        return fileType != null ? multimedia.fileType.eq(FileType.valueOf(fileType)) : null;
    }

    private BooleanExpression postIdEquals(UUID postId) {
        return postId != null ? multimedia.post.postId.eq(postId) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return multimedia.createdAt.between(start, end);
        } else if (start != null) {
            return multimedia.createdAt.goe(start);
        } else if (end != null) {
            return multimedia.createdAt.loe(end);
        }
        return null;
    }

    private OrderSpecifier<?> getOrderBy(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return multimedia.createdAt.desc(); // 기본 정렬
        }

        for (Sort.Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "createdAt":
                    return order.isAscending() ? multimedia.createdAt.asc() : multimedia.createdAt.desc();
//                case "fileName":
//                    return order.isAscending() ? multimedia.fileName.asc() : multimedia.fileName.desc();
                case "fileSize":
                    return order.isAscending() ? multimedia.fileSize.asc() : multimedia.fileSize.desc();
            }
        }

        return multimedia.createdAt.desc(); // 기본 정렬
    }
}
