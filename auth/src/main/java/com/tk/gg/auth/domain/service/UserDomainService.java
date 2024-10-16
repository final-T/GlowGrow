package com.tk.gg.auth.domain.service;

import com.tk.gg.auth.application.dto.UserDto;
import com.tk.gg.auth.domain.model.User;
import com.tk.gg.auth.domain.repository.UserRepository;
import com.tk.gg.auth.presentation.response.TokenInfoResponse;
import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.security.config.UserInfoEncoder;
import com.tk.gg.security.jwt.JwtProvider;
import com.tk.gg.security.jwt.TokenInfo;
import com.tk.gg.security.jwt.TokenableUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.tk.gg.common.response.exception.GlowGlowError.AUTH_ALREADY_SING_UP;
import static com.tk.gg.common.response.exception.GlowGlowError.USER_NO_EXIST;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final JwtProvider jwtProvider;
    private final UserInfoEncoder userInfoEncoder;
    private final UserRepository userRepository;


    @Transactional
    public void saveUser(UserDto dto) {
        if(userRepository.findByEmail(dto.email()).isPresent())
            throw new GlowGlowException(AUTH_ALREADY_SING_UP);
        userRepository.save(dto.toEntity(userInfoEncoder.hashed(dto.password())));
    }

    @Transactional
    public TokenInfoResponse login(UserDto dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new GlowGlowException(USER_NO_EXIST));

       if(!userInfoEncoder.matches(dto.password(), user.getPassword())){
           throw new GlowGlowException(GlowGlowError.AUTH_INVALID_CREDENTIALS);
       };

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
