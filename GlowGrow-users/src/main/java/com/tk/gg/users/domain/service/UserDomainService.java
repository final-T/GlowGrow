package com.tk.gg.users.domain.service;

import com.tk.gg.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public boolean findByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
