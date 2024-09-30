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

    public TokenInfoResponse singUp(UserDto dto) {
        return userDomainService.saveUser(dto);
    }
}
