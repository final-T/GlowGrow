package com.tk.gg.payment.application.client;

import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.payment.infrastructure.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserFeignClient userFeignClient;

    @Override
    public boolean isUserExistsByEmail(String email) {
        GlobalResponse<Boolean> response = userFeignClient.findByEmail(email);
        log.info("Feign response for email {}: data={}, message={}",
                email, response.getData(), response.getMessage());
        return response.getData();
    }
}
