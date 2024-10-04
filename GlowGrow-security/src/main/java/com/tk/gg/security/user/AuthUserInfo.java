package com.tk.gg.security.user;

import com.tk.gg.security.jwt.TokenableInfo;

public interface AuthUserInfo extends TokenableInfo {
    String getToken();
}
