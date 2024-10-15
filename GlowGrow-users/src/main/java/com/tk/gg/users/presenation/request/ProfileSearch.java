package com.tk.gg.users.presenation.request;

import com.tk.gg.common.enums.UserRole;

import java.util.List;

public record ProfileSearch(
        List<String> styleList,
        List<Long> priceList,
        List<String> locationList,
        UserRole role
) {
}
