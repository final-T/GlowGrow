package com.tk.gg.users.application.service;

import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.domain.service.UserDomainService;
import com.tk.gg.users.presenation.request.UpdateUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;

    public boolean findByEmail(String email) {
        return userDomainService.findByEmail(email);
    }

    public UserDto updateUserInfo(AuthUserInfo authUserInfo, UpdateUserInfoRequest request) {
        return userDomainService.updateUserInfo(authUserInfo, request);
    }
}
