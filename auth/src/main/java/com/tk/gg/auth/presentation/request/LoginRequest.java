package com.tk.gg.auth.presentation.request;

import com.tk.gg.auth.application.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email은 꼭 필요합니다")
        @Email
        String email,
        @NotBlank(message = "비밀번호는 필수사항입니다.")
        String password
) {
    public UserDto toDto() {
        return UserDto.loginOf(email, password);
    }
}
