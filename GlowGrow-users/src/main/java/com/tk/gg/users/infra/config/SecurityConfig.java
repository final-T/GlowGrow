package com.tk.gg.users.infra.config;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.security.hooks.SecurityRequestMatcher;
import com.tk.gg.security.hooks.SecurityRequestMatcherChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityRequestMatcherChain securityRequestMatcherChain() {
        SecurityRequestMatcherChain securityRequestMatcherChain = new SecurityRequestMatcherChain();
        securityRequestMatcherChain
                .add(SecurityRequestMatcher.hasAnyRolesOf(
                        List.of(UserRole.CUSTOMER, UserRole.PROVIDER, UserRole.MASTER),
                        "/api/profile/**"));
        return securityRequestMatcherChain;
    }
}
