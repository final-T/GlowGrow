package com.tk.gg.users.infra.repository.profile;

import com.tk.gg.users.presenation.request.ProfileSearch;
import com.tk.gg.users.presenation.response.ProfilePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileQueryDslRepository {
    Page<ProfilePageResponse> searchProfiles(ProfileSearch profileSearch, Pageable pageable);
}
