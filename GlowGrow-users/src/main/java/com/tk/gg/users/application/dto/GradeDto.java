package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.Grade;
import com.tk.gg.users.domain.type.UserGradeType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record GradeDto(
        UUID gradeId,
        UserGradeType gradeType,
        String gradeName,
        Double gradeMaxScore,
        Double gradeMinScore,
        String gradeImageUrl,
        String gradeInfo,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static GradeDto from(Grade entity) {
        return GradeDto.builder()
                .gradeId(entity.getGradeId())
                .gradeType(entity.getUserGradeType())
                .gradeName(entity.getGradeName())
                .gradeMaxScore(entity.getGradeMaxScore())
                .gradeMinScore(entity.getGradeMinScore())
                .gradeImageUrl(entity.getGradeImageUrl())
                .gradeInfo(entity.getGradeInfo())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}
