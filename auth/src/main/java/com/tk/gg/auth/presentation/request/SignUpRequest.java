package com.tk.gg.auth.presentation.request;

import com.tk.gg.auth.application.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SignUpRequest(

        @NotBlank(message = "email은 꼭 필요합니다")
        @Email
        String email,

        @NotBlank(message = "성함은 비어있으면 안됩니다")
        @Length(min = 1, max = 255)
        String username,

        @NotBlank(message = "비밀번호는 필수사항입니다.")
        String password,

        @NotBlank
        String userRole,

        @NotBlank
        String gender,

        String phoneNumber,
        String address
) {
        public UserDto toDto() {
                return UserDto.signUpOf(email, username, password, userRole, gender, phoneNumber, address);
        }
}
