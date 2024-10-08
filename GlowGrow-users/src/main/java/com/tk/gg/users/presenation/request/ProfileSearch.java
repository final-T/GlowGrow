package com.tk.gg.users.presenation.request;

import com.tk.gg.common.enums.UserRole;

import java.util.List;
import java.util.UUID;

public record ProfileSearch(
        List<String> styleList,
        List<Long> priceList,
        List<String> locationList,
        UserRole role
) {
}
