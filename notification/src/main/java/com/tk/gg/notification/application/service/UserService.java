package com.tk.gg.notification.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.notification.application.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;

    public void checkUserExists(String email) {
        if (!userClient.findByEmail(email).getData()) {
            throw new GlowGlowException(GlowGlowError.AWARD_NO_EXIST);
        }
    }
}
