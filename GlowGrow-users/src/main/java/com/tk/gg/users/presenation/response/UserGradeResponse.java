package com.tk.gg.users.presenation.response;

import com.tk.gg.users.application.dto.GradeDto;
import com.tk.gg.users.application.dto.UserGradeDto;
import com.tk.gg.users.domain.type.UserGradeType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserGradeResponse(
        UUID userGradeId,
        UserGradeType userGradeType,
        String gradeName,
        String gradeImageUrl,
        LocalDateTime updatedAt
) {
    public static UserGradeResponse from(UserGradeDto userGradeDto, GradeDto gradeDto) {
        return UserGradeResponse.builder()
                .userGradeId(userGradeDto.userGradeId())
                .userGradeType(userGradeDto.userGradeType())
                .gradeName(gradeDto.gradeName())
                .gradeImageUrl(gradeDto.gradeImageUrl())
                .updatedAt(userGradeDto.updatedAt())
                .build();
    }
}
