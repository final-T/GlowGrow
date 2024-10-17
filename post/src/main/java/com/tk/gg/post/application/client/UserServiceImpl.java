package com.tk.gg.post.application.client;

import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.post.infrastructure.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserFeignClient userFeignClient;

    @Override
    public boolean isUserExistsByEmail(String email) {
        GlobalResponse<Boolean> response = userFeignClient.findByEmail(email);
        return response.getData();
    }
}
