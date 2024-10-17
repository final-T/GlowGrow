package com.tk.gg.reservation.infrastructure.client;


import com.tk.gg.common.response.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/feign/user/exist/{email}")
    GlobalResponse<Boolean> findByEmail(@PathVariable("email") String email);
}
