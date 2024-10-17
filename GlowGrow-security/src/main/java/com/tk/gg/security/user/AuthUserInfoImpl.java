package com.tk.gg.security.user;

import com.tk.gg.common.enums.UserRole;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AuthUserInfoImpl implements AuthUserInfo {
    private Long id;
    private String email;
    private String username;
    private UserRole userRole;
    private String token;
}