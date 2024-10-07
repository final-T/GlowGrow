package com.tk.gg.users.presenation.request;

import com.tk.gg.users.application.dto.*;

import java.util.List;

public record CreateProfileRequest(
        ProfileRequest profileRequest,
        List<AwardRequest> awardRequestList,
        List<LocationRequest> locationRequestList,
        List<PriceRequest> priceRequestList,
        List<StyleRequest> styleRequestList,
        List<WorkExperienceRequest> workExperienceRequestList
) {
    public ProfileDto toProfileDto(UserDto userDto) {
        return ProfileDto.of(userDto, profileRequest);
    }

    public List<AwardDto> toAwardDto() {
        return awardRequestList.stream().map(AwardRequest::toDto).toList();
    }

    public List<PreferLocationDto> toPreferLocationDto() {
        return locationRequestList.stream().map(LocationRequest::toDto).toList();
    }

    public List<PreferPriceDto> toPreferPriceDto() {
        return priceRequestList.stream().map(PriceRequest::toDto).toList();
    }

    public List<PreferStyleDto> toPreferStyleDto() {
        return styleRequestList.stream().map(StyleRequest::toDto).toList();
    }

    public List<WorkExperienceDto> toWorkExperienceDto() {
        return workExperienceRequestList.stream().map(WorkExperienceRequest::toDto).toList();
    }
}
