package com.tk.gg.users.presenation;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.service.ProfileService;
import com.tk.gg.users.presenation.request.CreateProfileRequest;
import com.tk.gg.users.presenation.request.ProfileSearch;
import com.tk.gg.users.presenation.response.ProfilePageResponse;
import com.tk.gg.users.presenation.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.PROFILE_CREATE_SUCCESS;
import static com.tk.gg.common.response.ResponseMessage.PROFILE_RETRIEVE_SUCCESS;

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

    @GetMapping
    public GlobalResponse<Page<ProfilePageResponse>> searchProfiles(
            ProfileSearch profileSearch,
            @PageableDefault(sort = {"createAt"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(), profileService.searchProfiles(profileSearch, pageable));
    }

    @GetMapping("/{profileId}")
    public GlobalResponse<ProfileResponse> getProfile(@PathVariable("profileId") UUID profileId) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(), ProfileResponse.from(profileService.getProfile(profileId)));
    }

    @GetMapping("/my-profile")
    public GlobalResponse<ProfileResponse> getMyProfile(@AuthUser AuthUserInfo userInfo) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(), ProfileResponse.from(profileService.getMyProfile(userInfo.getId())));
    }
}
