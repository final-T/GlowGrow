package com.tk.gg.users.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.users.application.dto.*;
import com.tk.gg.users.domain.model.*;
import com.tk.gg.users.domain.repository.profile.*;
import com.tk.gg.users.presenation.request.ProfileSearch;
import com.tk.gg.users.presenation.request.UpdateProfileRequest;
import com.tk.gg.users.presenation.response.ProfilePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.tk.gg.common.response.exception.GlowGlowError.*;

@Service
@RequiredArgsConstructor
public class ProfileDomainService {
    private final ProfileRepository profileRepository;
    private final AwardRepository awardRepository;
    private final PreferLocationRepository preferLocationRepository;
    private final PreferPriceRepository preferPriceRepository;
    private final PreferStyleRepository preferStyleRepository;
    private final WorkExperienceRepository workExperienceRepository;

    public ProfileDto saveProfile(ProfileDto profileDto) {
        if(profileRepository.findByUserUserIdAndIsDeletedFalse(profileDto.userDto().userId()).isPresent())
            throw new GlowGlowException(PROFILE_ALREADY_EXIST);
        Profile profile = profileRepository.save(profileDto.toEntity());
        return ProfileDto.from(profile);
    }

    @Transactional
    public void saveAward(
            ProfileDto profileDto, List<AwardDto> awardDtoList, List<PreferLocationDto> preferLocationDtoList,
            List<PreferPriceDto> preferPriceDtoList, List<PreferStyleDto> preferStyleDtoList, List<WorkExperienceDto> workExperienceDtoList
    ) {
        // 저장된 프로필 검색
        Profile profile = profileRepository.findById(profileDto.profileId()).orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        // 수상경력 저장
        List<Award> awardList = awardDtoList.stream()
                .map(awardDto -> awardDto.toEntity(profile)) // AwardDto의 toEntity 메서드를 호출
                .toList();
        awardRepository.saveAll(awardList);

        // 선호 지역 저장
        List<PreferLocation> preferLocationList = preferLocationDtoList.stream()
                .map(preferLocationDto -> preferLocationDto.toEntity(profile))
                .toList();
        preferLocationRepository.saveAll(preferLocationList);

        // 선호 가격 저장
        List<PreferPrice> preferPriceList = preferPriceDtoList.stream()
                .map(preferPriceDto -> preferPriceDto.toEntity(profile))
                .toList();
        preferPriceRepository.saveAll(preferPriceList);

        // 선호 스타일 저장
        List<PreferStyle> preferStyleList = preferStyleDtoList.stream()
                .map(preferStyleDto -> preferStyleDto.toEntity(profile))
                .toList();
        preferStyleRepository.saveAll(preferStyleList);

        // 경력 사항 저장
        List<WorkExperience> workExperienceList = workExperienceDtoList.stream()
                .map(workExperienceDto -> workExperienceDto.toEntity(profile))
                .toList();
        workExperienceRepository.saveAll(workExperienceList);
    }

    public Page<ProfilePageResponse> searchProfiles(ProfileSearch profileSearch, Pageable pageable) {
        return profileRepository.searchProfiles(profileSearch, pageable);
    }

    public ProfileDto getProfile(UUID profileId) {
        Profile profile = profileRepository.findByProfileIdAndIsDeletedFalse(profileId).orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));
        return ProfileDto.from(profile);
    }

    public ProfileDto getMyProfile(User user) {
        Profile profile = profileRepository.findByUserUserIdAndIsDeletedFalse(user.getUserId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));
        return ProfileDto.from(profile);
    }

    @Transactional
    public void deleteProfile(UserDto userDto, UUID profileId) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        profile.delete(userDto);
    }

    @Transactional
    public void deleteAward(UserDto userDto, UUID profileId, UUID awardId) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        Award award = awardRepository.findByAwardIdAndProfileProfileId(awardId, profile.getProfileId())
                .orElseThrow(() -> new GlowGlowException(AWARD_NO_EXIST));

        award.delete();
    }

    public void deletePrice(UserDto userDto, UUID profileId, UUID priceId) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        PreferPrice price = preferPriceRepository.findByPreferPriceIdAndProfileProfileIdAndIsDeletedFalse(priceId, profile.getProfileId())
                .orElseThrow(() -> new GlowGlowException(PREFER_PRICE_NO_EXIST));

        price.delete();
    }

    public void deleteLocation(UserDto userDto, UUID profileId, UUID locationId) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        PreferLocation location = preferLocationRepository.findByPreferLocationIdAndProfileProfileIdAndIsDeletedFalse(locationId, profile.getProfileId())
                .orElseThrow(() -> new GlowGlowException(PREFER_LOCATION_NO_EXIST));

        location.delete();
    }

    public void deleteStyle(UserDto userDto, UUID profileId, UUID styleId) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        PreferStyle preferStyle = preferStyleRepository.findByPreferStyleIdAndProfileProfileIdAndIsDeletedFalse(styleId, profile.getProfileId())
                .orElseThrow(() -> new GlowGlowException(PREFER_STYLE_NO_EXIST));

        preferStyle.delete();
    }

    public void deleteWorkExperience(UserDto userDto, UUID profileId, UUID workExperienceId) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        WorkExperience workExperience = workExperienceRepository.findByWorkExperienceIdAndProfileProfileIdAndIsDeletedFalse(workExperienceId, profile.getProfileId())
                .orElseThrow(() -> new GlowGlowException(WORK_EXPERIENCE_NO_EXIST));

        workExperience.delete();
    }

    public ProfileDto updateProfile(UserDto userDto, UUID profileId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        profile.update(userDto, request);

        return getProfile(profile.getProfileId());
    }

    public List<PreferLocationDto> addLocation(UserDto userDto, UUID profileId, PreferLocationDto dto) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        if(preferLocationRepository.existsByProfileProfileIdAndIsDeletedFalseAndLocationName(profile.getProfileId(), dto.locationName())){
            throw new GlowGlowException(PREFER_LOCATION_ALREADY_EXIST);
        }

        preferLocationRepository.save(dto.toEntity(profile));

        return preferLocationRepository.findAllByProfileProfileIdAndIsDeletedFalse(profile.getProfileId())
                .stream().map(PreferLocationDto::from).toList();
    }

    public List<PreferPriceDto> addPrice(UserDto userDto, UUID profileId, PreferPriceDto dto) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));
        if(preferPriceRepository.existsByProfileProfileIdAndIsDeletedFalseAndPrice(profile.getProfileId(), dto.price())){
            throw new GlowGlowException(PREFER_PRICE_ALREADY_EXIST);
        }
        preferPriceRepository.save(dto.toEntity(profile));

        return preferPriceRepository.findAllByProfileProfileIdAndIsDeletedFalse(profile.getProfileId())
                .stream().map(PreferPriceDto::from).toList() ;
    }

    public List<PreferStyleDto> addStyle(UserDto userDto, UUID profileId, PreferStyleDto dto) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));
        if(preferStyleRepository.existsByProfileProfileIdAndIsDeletedFalseAndStyleName(profile.getProfileId(), dto.styleName())){
            throw new GlowGlowException(PREFER_STYLE_ALREADY_EXIST);
        }
        preferStyleRepository.save(dto.toEntity(profile));

        return preferStyleRepository.findAllByProfileProfileIdAndIsDeletedFalse(profile.getProfileId())
                .stream().map(PreferStyleDto::from).toList();
    }

    public List<AwardDto> addAward(UserDto userDto, UUID profileId, AwardDto dto) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));

        boolean exists = awardRepository.existsByAwardNameAndAwardLevelAndAwardDateAndOrganization(
                dto.awardName(),
                dto.awardLevel(),
                dto.awardDate(),
                dto.organization()
        );

        if(exists) {
            throw new GlowGlowException(AWARD_ALREADY_EXIST);
        }

        awardRepository.save(dto.toEntity(profile));

        return awardRepository.findAllByProfileProfileIdAndIsDeletedFalse(profile.getProfileId())
                .stream().map(AwardDto::from).toList();
    }

    public List<WorkExperienceDto> addWorkExperience(UserDto userDto, UUID profileId, WorkExperienceDto dto) {
        Profile profile = profileRepository.findByProfileIdAndUserUserIdAndIsDeletedFalse(profileId, userDto.userId())
                .orElseThrow(() -> new GlowGlowException(PROFILE_NO_EXIST));
        if(workExperienceRepository.existsByProfileProfileIdAndIsDeletedFalseAndCompanyName(profile.getProfileId(), dto.companyName())){
            throw new GlowGlowException(WORK_EXPERIENCE_ALREADY_EXIST);
        }

        workExperienceRepository.save(dto.toEntity(profile));

        return workExperienceRepository.findAllByProfileProfileIdAndIsDeletedFalse(profile.getProfileId())
                .stream().map(WorkExperienceDto::from).toList();
    }
}
