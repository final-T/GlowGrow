package com.tk.gg.users.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.domain.model.User;
import com.tk.gg.users.domain.repository.user.UserRepository;
import com.tk.gg.users.presenation.request.UpdateUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.tk.gg.common.response.exception.GlowGlowError.USER_NO_EXIST;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDto findById(Long id) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(id).orElse(null);
        if(user == null) {
            return null;
        }

        return UserDto.from(user);
    }

    public boolean findByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserDto updateUserInfo(AuthUserInfo authUserInfo, UpdateUserInfoRequest request) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(authUserInfo.getId())
                .orElseThrow(() -> new GlowGlowException(USER_NO_EXIST));

        user.updateInfo(request);
        return UserDto.from(userRepository.findByEmail(authUserInfo.getEmail()).orElseThrow(() -> new GlowGlowException(USER_NO_EXIST)));
    }
}
