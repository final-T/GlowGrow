package com.tk.gg.payment.infrastructure.config;

import com.tk.gg.security.hooks.SecurityRequestMatcher;
import com.tk.gg.security.hooks.SecurityRequestMatcherChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.tk.gg.common.enums.UserRole.MASTER;
import static com.tk.gg.common.enums.UserRole.PROVIDER;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityRequestMatcherChain securityRequestMatcherChain() {
        SecurityRequestMatcherChain matcherChain = new SecurityRequestMatcherChain();
        matcherChain.addAll(
                SecurityRequestMatcher.permitAllOf("/api/auth/**"),
                SecurityRequestMatcher.permitAllOf("/api/payments/client-key"),
                SecurityRequestMatcher.permitAllOf("/api/payments/prepare"),

        // 정산 생성 - MASTER 권한만 가능
        SecurityRequestMatcher.hasAnyRolesOf(
                List.of(MASTER),
                POST,
                "/api/settlements"
        ),

                // 단일 정산 조회 - MASTER 권한만 가능
                SecurityRequestMatcher.hasAnyRolesOf(
                        List.of(MASTER),
                        GET,
                        "/api/settlements/{settlementId}"
                ),

                // 정산 수정 - MASTER 권한만 가능
                SecurityRequestMatcher.hasAnyRolesOf(
                        List.of(MASTER),
                        PATCH,
                        "/api/settlements/{settlementId}"
                ),

                // 정산 검색 - MASTER, PROVIDER 권한 가능 (CUSTOMER 불가)
                SecurityRequestMatcher.hasAnyRolesOf(
                        List.of(MASTER, PROVIDER),
                        GET,
                        "/api/settlements/search"
                )
        );
        return matcherChain;
    }
}