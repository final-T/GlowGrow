package com.tk.gg.users.application.dto;

import com.tk.gg.common.enums.Gender;
import com.tk.gg.common.enums.UserRole;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record UserDto(
        Long userId,
        String email,
        String username,
        String password,
        UserRole role,
        Gender gender,
        String phoneNumber,
        String address,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {

}
