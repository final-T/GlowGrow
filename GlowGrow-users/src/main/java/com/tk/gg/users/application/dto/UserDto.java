package com.tk.gg.users.application.dto;

import com.tk.gg.common.enums.Gender;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.users.domain.model.User;
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
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {

    public static UserDto from(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .deletedAt(user.getDeletedAt())
                .deletedBy(user.getDeletedBy())
                .build();
    }

    public static UserDto of(String username, String phoneNumber, String address) {
        return UserDto.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
    }
}
