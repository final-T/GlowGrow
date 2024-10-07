package com.tk.gg.users.domain.service;

import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.domain.model.User;
import com.tk.gg.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return null;
        }

        return UserDto.from(user);
    }

    public boolean findByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
