package com.tk.gg.users.domain.service;

import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.users.application.dto.*;
import com.tk.gg.users.domain.model.*;
import com.tk.gg.users.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tk.gg.common.response.exception.GlowGlowError.PROFILE_NO_EXIST;

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
}
