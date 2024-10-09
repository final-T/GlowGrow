package com.tk.gg.payment.infrastructure.config;

import com.tk.gg.security.hooks.SecurityRequestMatcher;
import com.tk.gg.security.hooks.SecurityRequestMatcherChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityRequestMatcherChain securityRequestMatcherChain() {
        SecurityRequestMatcherChain matcherChain = new SecurityRequestMatcherChain();
        matcherChain
                .add(SecurityRequestMatcher.permitAllOf("/api/auth/**"))
                //.add(SecurityRequestMatcher.permitAllOf("/api/payments"))
        ;

        return matcherChain;
    }
}