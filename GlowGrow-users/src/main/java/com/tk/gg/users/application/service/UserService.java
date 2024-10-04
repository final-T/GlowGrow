package com.tk.gg.users.application.service;

import com.tk.gg.users.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;

    public boolean findByEmail(String email) {
        return userDomainService.findByEmail(email);
    }
}
