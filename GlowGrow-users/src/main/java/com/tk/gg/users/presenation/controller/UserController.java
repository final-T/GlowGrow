package com.tk.gg.users.presenation.controller;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.service.GradeService;
import com.tk.gg.users.application.service.UserService;
import com.tk.gg.users.presenation.request.UpdateUserInfoRequest;
import com.tk.gg.users.presenation.response.UserGradeResponse;
import com.tk.gg.users.presenation.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.tk.gg.common.response.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final GradeService gradeService;

    @PutMapping
    public GlobalResponse<UserResponse> updateUserInfo(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestBody UpdateUserInfoRequest request
    ) {
        return ApiUtils.success(USER_UPDATE_SUCCESS.getMessage(),
                UserResponse.from(userService.updateUserInfo(authUserInfo, request)));
    }

    @GetMapping("/grade/my")
    public GlobalResponse<UserGradeResponse> getMyGrade(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        return ApiUtils.success(USER_GRADE_RETRIEVE_SUCCESS.getMessage(),
                gradeService.getMyGrade(authUserInfo));
    }

    @DeleteMapping
    public GlobalResponse<String> deleteUser(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestParam("password") String password
    ) {
        userService.deleteUser(authUserInfo, password);
        return ApiUtils.success(USER_DELETE_SUCCESS.getMessage());
    }
}
