package com.tk.gg.auth.application.service;

import com.tk.gg.auth.application.dto.UserDto;
import com.tk.gg.auth.domain.service.UserDomainService;
import com.tk.gg.auth.presentation.response.TokenInfoResponse;
import com.tk.gg.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDomainService userDomainService;

    public void singUp(UserDto dto) {
        userDomainService.saveUser(dto);
    }

    public TokenInfoResponse login(UserDto dto) {
        return userDomainService.login(dto);
    }
}
