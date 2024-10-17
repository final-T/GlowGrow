package com.tk.gg.reservation.application.client;

import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.reservation.infrastructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    //TODO : resilience4j 재시도 적용할까?
    @Override
    public boolean isUserExistsByEmail(String email) {
        GlobalResponse<Boolean> response = userClient.findByEmail(email);
        return response.getData();
    }
}
