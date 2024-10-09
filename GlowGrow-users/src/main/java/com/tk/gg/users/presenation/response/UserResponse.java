package com.tk.gg.users.presenation.response;

import com.tk.gg.common.enums.Gender;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.users.application.dto.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long userId,
        String email,
        String username,
        UserRole role,
        Gender gender,
        String phoneNumber,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(UserDto dto){
        return UserResponse.builder()
                .userId(dto.userId())
                .email(dto.email())
                .username(dto.username())
                .gender(dto.gender())
                .phoneNumber(dto.phoneNumber())
                .address(dto.address())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }
}
