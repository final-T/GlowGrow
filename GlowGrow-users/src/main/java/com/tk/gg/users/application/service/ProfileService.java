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
import com.tk.gg.users.presenation.request.ProfileSearch;
import com.tk.gg.users.presenation.response.ProfilePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileDomainService profileDomainService;
    private final UserDomainService userDomainService;

    @Transactional
    public void createProfile(AuthUserInfo userInfo, CreateProfileRequest request) {
        UserDto userDto = checkUser(userInfo);

        ProfileDto profileDto = profileDomainService.saveProfile(request.toProfileDto(userDto));

        profileDomainService.saveAward(
                profileDto, request.toAwardDto(), request.toPreferLocationDto(),
                request.toPreferPriceDto(), request.toPreferStyleDto(), request.toWorkExperienceDto()
        );
    }

    @Transactional(readOnly = true)
    public Page<ProfilePageResponse> searchProfiles(ProfileSearch profileSearch, Pageable pageable) {
        return profileDomainService.searchProfiles(profileSearch, pageable);
    }

    @Transactional(readOnly = true)
    public ProfileDto getProfile(UUID profileId) {
        return profileDomainService.getProfile(profileId);
    }

    @Transactional(readOnly = true)
    public ProfileDto getMyProfile(AuthUserInfo userInfo) {
        UserDto userDto = checkUser(userInfo);
        return profileDomainService.getMyProfile(User.of(userDto));
    }

    public void deleteProfile(AuthUserInfo userInfo, UUID profileId) {
        UserDto userDto = checkUser(userInfo);

        profileDomainService.deleteProfile(userDto, profileId);
    }

    public void deleteAward(AuthUserInfo userInfo, UUID profileId, UUID awardId) {
        UserDto userDto = checkUser(userInfo);
        profileDomainService.deleteAward(userDto, profileId, awardId);
    }

    public void deletePrice(AuthUserInfo userInfo, UUID profileId, UUID priceId) {
        UserDto userDto = checkUser(userInfo);
        profileDomainService.deletePrice(userDto, profileId, priceId);
    }

    public void deleteLocation(AuthUserInfo userInfo, UUID profileId, UUID locationId) {
        UserDto userDto = checkUser(userInfo);
        profileDomainService.deleteLocation(userDto, profileId, locationId);
    }

    public void deleteStyle(AuthUserInfo userInfo, UUID profileId, UUID styleId) {
        UserDto userDto = checkUser(userInfo);
        profileDomainService.deleteStyle(userDto, profileId, styleId);
    }

    public void deleteWorkExperience(AuthUserInfo userInfo, UUID profileId, UUID workExperienceId) {
        UserDto userDto = checkUser(userInfo);
        profileDomainService.deleteWorkExperience(userDto, profileId, workExperienceId);
    }

    private UserDto checkUser(AuthUserInfo userInfo) {
        UserDto userDto = userDomainService.findById(userInfo.getId());
        if(userDto == null) {
            throw new GlowGlowException(GlowGlowError.AUTH_UNAUTHORIZED);
        }

        return userDto;
    }

}
