package com.tk.gg.users.presenation;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.security.user.AuthUser;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.users.application.service.ProfileService;
import com.tk.gg.users.presenation.request.*;
import com.tk.gg.users.presenation.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public GlobalResponse<String> createProfile(
            @AuthUser AuthUserInfo userInfo,
            @RequestBody CreateProfileRequest request
    ) {
        profileService.createProfile(userInfo, request);
        return ApiUtils.success(PROFILE_CREATE_SUCCESS.getMessage());
    }

    @GetMapping
    public GlobalResponse<Page<ProfilePageResponse>> searchProfiles(
            ProfileSearch profileSearch,
            @PageableDefault(sort = {"createAt"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                profileService.searchProfiles(profileSearch, pageable));
    }

    @GetMapping("/{profileId}")
    public GlobalResponse<ProfileResponse> getProfile(
            @PathVariable("profileId") UUID profileId
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                ProfileResponse.from(profileService.getProfile(profileId)));
    }

    @GetMapping("/my")
    public GlobalResponse<ProfileResponse> getMyProfile(
            @AuthUser AuthUserInfo userInfo
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                ProfileResponse.from(profileService.getMyProfile(userInfo)));
    }

    /**
     * 프로필 관련 업데이트
     * @param userInfo
     * @param profileId
     * @param request
     * @return
     */
    @PutMapping("/{profileId}")
    public GlobalResponse<ProfileResponse> updateProfile(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @RequestBody UpdateProfileRequest request
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                ProfileResponse.from(profileService.updateProfile(userInfo, profileId, request))
        );
    }

    /**
     * 선호지역 추가
     *
     * @param userInfo
     * @param profileId
     * @param request
     * @return
     */
    @PostMapping("/{profileId}/location")
    public GlobalResponse<List<LocationResponse>> addLocation(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @RequestBody LocationRequest request
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                profileService.addLocation(userInfo, profileId, request).stream().map(LocationResponse::from).toList()
        );
    }

    /**
     * 선호 가격 추가
     *
     * @param userInfo
     * @param profileId
     * @param request
     * @return
     */
    @PostMapping("/{profileId}/price")
    public GlobalResponse<List<PriceResponse>> addPrice(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @RequestBody PriceRequest request
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                profileService.addPrice(userInfo, profileId, request).stream().map(PriceResponse::from).toList()
        );
    }

    /** 선호 스타일 추가
     *
     * @param userInfo
     * @param profileId
     * @param request
     * @return
     */
    @PostMapping("/{profileId}/award")
    public GlobalResponse<List<StyleResponse>> addStyle(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @RequestBody StyleRequest request
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                profileService.addStyle(userInfo, profileId, request).stream().map(StyleResponse::from).toList()
        );
    }

    /**
     * 수상 경력 추가
     *
     * @param userInfo
     * @param profileId
     * @param request
     * @return
     */
    @PostMapping("/{profileId}/award")
    public GlobalResponse<List<AwardResponse>> addAward(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @RequestBody AwardRequest request
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                profileService.addAward(userInfo, profileId, request).stream().map(AwardResponse::from).toList()
        );
    }

    /**
     * 경력 사항 추가
     *
     * @param userInfo
     * @param profileId
     * @param request
     * @return
     */
    @PostMapping("/{profileId}/award")
    public GlobalResponse<List<WorkExperienceResponse>> addWorkExperience(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @RequestBody WorkExperienceRequest request
    ) {
        return ApiUtils.success(PROFILE_RETRIEVE_SUCCESS.getMessage(),
                profileService.addWorkExperience(userInfo, profileId, request).stream().map(WorkExperienceResponse::from).toList()
        );
    }

    /**
     * 프로필 완전 삭제
     *
     * @param userInfo
     * @param profileId
     *
     * @return 성공 응답
     */
    @DeleteMapping("/{profileId}/delete")
    public GlobalResponse<String> deleteProfile(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId
    ){
        profileService.deleteProfile(userInfo, profileId);
        return ApiUtils.success(PROFILE_DELETE_SUCCESS.getMessage());
    }

    /**
     * 선호 가격 삭제
     *
     * @param userInfo
     * @param profileId
     * @param priceId
     * @return
     */

    @DeleteMapping("/{profileId}/delete/price/{priceId}")
    public GlobalResponse<String> deletePrice(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @PathVariable("priceId") UUID priceId
    ){
        profileService.deletePrice(userInfo, profileId, priceId);
        return ApiUtils.success(PROFILE_DELETE_SUCCESS.getMessage());
    }

    /**
     * 선호 지역 삭제
     *
     * @param userInfo
     * @param profileId
     * @param locationId
     * @return
     */
    @DeleteMapping("/{profileId}/delete/location/{locationId}")
    public GlobalResponse<String> deleteLocation(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @PathVariable("locationId") UUID locationId
    ){
        profileService.deleteLocation(userInfo, profileId, locationId);
        return ApiUtils.success(PROFILE_DELETE_SUCCESS.getMessage());
    }

    /**
     * 선호 스타일 삭제
     *
     * @param userInfo
     * @param profileId
     * @param styleId
     * @return
     */
    @DeleteMapping("/{profileId}/delete/style/{styleId}")
    public GlobalResponse<String> deleteStyle(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @PathVariable("styleId") UUID styleId
    ){
        profileService.deleteStyle(userInfo, profileId, styleId);
        return ApiUtils.success(PROFILE_DELETE_SUCCESS.getMessage());
    }

    /**
     * 수상 경력 삭제
     *
     * @param userInfo
     * @param profileId
     * @param awardId
     * @return
     */
    @DeleteMapping("/{profileId}/delete/award/{awardId}")
    public GlobalResponse<String> deleteAward(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @PathVariable("awardId") UUID awardId
    ){
        profileService.deleteAward(userInfo, profileId, awardId);
        return ApiUtils.success(PROFILE_DELETE_SUCCESS.getMessage());
    }

    /**
     * 경력사항 삭제
     *
     * @param userInfo
     * @param profileId
     * @param workExperienceId
     * @return
     */
    @DeleteMapping("/{profileId}/delete/experience/{workExperienceId}")
    public GlobalResponse<String> deleteWorkExperience(
            @AuthUser AuthUserInfo userInfo,
            @PathVariable("profileId") UUID profileId,
            @PathVariable("workExperienceId") UUID workExperienceId
    ){
        profileService.deleteWorkExperience(userInfo, profileId, workExperienceId);
        return ApiUtils.success(PROFILE_DELETE_SUCCESS.getMessage());
    }
}
