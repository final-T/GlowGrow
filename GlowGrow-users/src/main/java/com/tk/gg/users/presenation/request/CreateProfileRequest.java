package com.tk.gg.users.presenation.request;

import java.util.List;

public record CreateProfileRequest(
        ProfileRequest profileRequest,
        List<AwardRequest> awardRequestList,
        List<LocationRequest> locationRequestList,
        List<PriceRequest> priceRequestList,
        List<StyleRequest> styleRequestList,
        List<WorkExperienceRequest> workExperienceRequestList
) {
}
