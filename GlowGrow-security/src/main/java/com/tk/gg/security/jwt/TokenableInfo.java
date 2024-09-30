package com.tk.gg.security.jwt;

import com.tk.gg.common.enums.Gender;
import com.tk.gg.common.enums.UserRole;


public interface TokenableInfo {
    Long getId();
    String getUsername();
    String getEmail();
    UserRole getUserRole();
}