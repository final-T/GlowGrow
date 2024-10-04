package com.tk.gg.auth.presentation.controller;

import com.tk.gg.auth.application.service.AuthService;
import com.tk.gg.auth.domain.model.User;
import com.tk.gg.auth.presentation.request.LoginRequest;
import com.tk.gg.auth.presentation.request.SignUpRequest;
import com.tk.gg.auth.presentation.response.TokenInfoResponse;
import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public GlobalResponse<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.singUp(signUpRequest.toDto());
        return ApiUtils.success(ResponseMessage.AUTH_SING_UP_SUCCESS.getMessage());
    }


    @PostMapping("/login")
    public GlobalResponse<TokenInfoResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        return ApiUtils.success(ResponseMessage.AUTH_LOGIN_SUCCESS.getMessage(), authService.login(loginRequest.toDto()));
    }

    @GetMapping("/info")
    public GlobalResponse<String> info(@AuthUser AuthUserInfo authUser) {
        System.out.println(" " + authUser.getId());
        return ApiUtils.success(authUser.getToken());
    }
}
