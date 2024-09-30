package com.tk.gg.auth.presentation.controller;

import com.tk.gg.auth.application.service.AuthService;
import com.tk.gg.auth.presentation.request.SignUpRequest;
import com.tk.gg.auth.presentation.response.TokenInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        TokenInfoResponse response = authService.singUp(signUpRequest.toDto());
    }


    @PostMapping("/login")
    public void login(){}
}
