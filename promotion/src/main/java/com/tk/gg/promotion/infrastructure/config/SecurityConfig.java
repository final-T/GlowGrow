package com.tk.gg.promotion.infrastructure.config;

import com.tk.gg.security.hooks.SecurityRequestMatcher;
import com.tk.gg.security.hooks.SecurityRequestMatcherChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.tk.gg.common.enums.UserRole.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityRequestMatcherChain securityRequestMatcherChain() {
        SecurityRequestMatcherChain matcherChain = new SecurityRequestMatcherChain();
        matcherChain.addAll(
                // Promotion
                SecurityRequestMatcher.authenticatedOf(GET, "/api/promotions/**"), // 조회
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), POST, "/api/promotions"), // 생성
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), PUT, "/api/promotions/{promotionId}"), // 수정
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), DELETE, "/api/promotions/{promotionId}") // 삭제
        );
        matcherChain.addAll(
                // Coupon
                SecurityRequestMatcher.authenticatedOf(GET, "/api/promotions/**"),
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), POST, "/api/coupons"), // 생성
                SecurityRequestMatcher.authenticatedOf(POST, "/api/coupons/{couponId}/issue") // 발급
        );
        matcherChain.addAll(
                // CouponUser
                SecurityRequestMatcher.authenticatedOf(GET, "/api/my-coupons"), // 조회
                SecurityRequestMatcher.authenticatedOf(GET, "/api/my-coupons/{couponId}"), // 단건 조회
                SecurityRequestMatcher.authenticatedOf(PATCH, "/api/my-coupons/{couponId}/use") // 사용
        );

        return matcherChain;
    }
}
