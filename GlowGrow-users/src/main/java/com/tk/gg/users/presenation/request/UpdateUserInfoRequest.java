package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.UserDto;

public record UpdateUserInfoRequest(
        String username,
        String phoneNumber,
        String address
) {
    public UserDto toDto() {
        return UserDto.of(username, phoneNumber, address);
    }
}
