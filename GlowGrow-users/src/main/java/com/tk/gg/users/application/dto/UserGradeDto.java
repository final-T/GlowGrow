package com.tk.gg.users.application.dto;

import com.tk.gg.users.domain.model.UserGrade;
import com.tk.gg.users.domain.type.UserGradeType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserGradeDto(
        UUID userGradeId,
        UserDto userDto,
        UserGradeType userGradeType,
        Double score,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static UserGradeDto from(UserGrade entity) {
        return UserGradeDto.builder()
                .userGradeId(entity.getUserGradeId())
                .userDto(UserDto.from(entity.getUser()))
                .userGradeType(entity.getUserGradeType())
                .score(entity.getScore())
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
