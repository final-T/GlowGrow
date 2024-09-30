package com.tk.gg.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoEncoder {
    private final PasswordEncoder passwordEncoder;

    public String hashed(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
