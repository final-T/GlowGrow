package com.tk.gg.security.jwt;

import com.tk.gg.common.enums.UserRole;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class TokenableUser implements TokenableInfo {
    private Long id;
    private String email;
    private String username;
    private UserRole userRole;
}
