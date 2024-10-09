package com.tk.gg.users.presenation;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.application.service.UserService;
import com.tk.gg.users.presenation.request.UpdateUserInfoRequest;
import com.tk.gg.users.presenation.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tk.gg.common.response.ResponseMessage.USER_UPDATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PutMapping
    public GlobalResponse<UserResponse> updateUserInfo(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestBody UpdateUserInfoRequest request
    ) {
        return ApiUtils.success(USER_UPDATE_SUCCESS.getMessage(),
                UserResponse.from(userService.updateUserInfo(authUserInfo, request)));
    }

}
