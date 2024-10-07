package com.tk.gg.users.application.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.dto.ProfileDto;
import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.domain.model.User;
import com.tk.gg.users.domain.service.ProfileDomainService;
import com.tk.gg.users.domain.service.UserDomainService;
import com.tk.gg.users.presenation.request.CreateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileDomainService profileDomainService;
    private final UserDomainService userDomainService;

    public void createProfile(AuthUserInfo userInfo, CreateProfileRequest request) {
        UserDto userDto = userDomainService.findById(userInfo.getId());
        if(userDto == null) {
            throw new GlowGlowException(GlowGlowError.AUTH_UNAUTHORIZED);
        }

        ProfileDto profileDto = profileDomainService.saveProfile(request.toProfileDto(userDto));

        profileDomainService.saveAward(
                profileDto, request.toAwardDto(), request.toPreferLocationDto(),
                request.toPreferPriceDto(), request.toPreferStyleDto(), request.toWorkExperienceDto()
        );
    }
}
