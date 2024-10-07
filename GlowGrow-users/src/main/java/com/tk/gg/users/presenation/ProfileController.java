package com.tk.gg.users.presenation;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.service.ProfileService;
import com.tk.gg.users.presenation.request.CreateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tk.gg.common.response.ResponseMessage.PROFILE_CREATE_SUCCESS;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public GlobalResponse<String> createProfile(@AuthUser AuthUserInfo userInfo, @RequestBody CreateProfileRequest request) {
        profileService.createProfile(userInfo, request);
        return ApiUtils.success(PROFILE_CREATE_SUCCESS.getMessage());
    }
}
