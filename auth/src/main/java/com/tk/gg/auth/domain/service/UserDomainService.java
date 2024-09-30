package com.tk.gg.auth.domain.service;

import com.tk.gg.auth.application.dto.UserDto;
import com.tk.gg.auth.domain.model.User;
import com.tk.gg.auth.domain.repository.UserRepository;
import com.tk.gg.auth.presentation.response.TokenInfoResponse;
import com.tk.gg.security.config.UserInfoEncoder;
import com.tk.gg.security.jwt.JwtProvider;
import com.tk.gg.security.jwt.TokenInfo;
import com.tk.gg.security.jwt.TokenableUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final JwtProvider jwtProvider;
    private final UserInfoEncoder userInfoEncoder;
    private final UserRepository userRepository;


    @Transactional
    public TokenInfoResponse saveUser(UserDto dto) {
        User user = userRepository.save(dto.toEntity(userInfoEncoder.hashed(dto.password())));

        return toTokenInfoServiceResponse(
                generateTokenAndSaveRefresh(user), user
        );
    }

    private TokenInfo generateTokenAndSaveRefresh(User user) {

        return jwtProvider.generateToken(TokenableUser.builder()
                .id(user.getUserId())
                .userRole(user.getRole())
                .username(user.getUsername())
                .email(user.getEmail())
                .build());
    }

    private TokenInfoResponse toTokenInfoServiceResponse(final TokenInfo tokenInfo, User user) {
        return TokenInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }
}
