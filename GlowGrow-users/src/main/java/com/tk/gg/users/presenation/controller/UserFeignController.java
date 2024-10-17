package com.tk.gg.users.presenation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.users.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feign/user")
@Slf4j(topic = "USER_FEIGN_CONTROLLER")
@RequiredArgsConstructor
public class UserFeignController {

    private final UserService userService;

    @GetMapping("/exist/{email}")
    public GlobalResponse<Boolean> findByEmail(@PathVariable("email") String email) {

        return ApiUtils.success("사용자가 있습니다.", userService.findByEmail(email));
    }
}
