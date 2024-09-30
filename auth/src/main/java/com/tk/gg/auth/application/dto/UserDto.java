package com.tk.gg.auth.application.dto;

import com.tk.gg.auth.domain.model.User;
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
    public static UserDto signUpOf(
            String email, String username, String password,
            String role, String gender, String phoneNumber, String address
    ) {
        return UserDto.builder()
                .email(email)
                .username(username)
                .password(password)
                .role(UserRole.fromString(role))
                .gender(Gender.fromString(gender))
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
    }

    public User toEntity(String encodePassword) {
        return User.create(email, username, encodePassword, role, gender, phoneNumber, address);
    }
}
