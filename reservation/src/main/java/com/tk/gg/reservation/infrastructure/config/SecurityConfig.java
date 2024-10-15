package com.tk.gg.reservation.infrastructure.config;

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
        matcherChain.addAll( // TimeSlots
                SecurityRequestMatcher.authenticatedOf(GET, "/api/time-slots/**"), // 조회
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), POST, "/api/time-slots"), // 생성
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), PUT, "/api/time-slots/{timeSlotId}"), // 수정
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), DELETE, "/api/time-slots/{timeSlotId}") // 삭제
        );
        matcherChain.addAll( // Reservation
                SecurityRequestMatcher.authenticatedOf(GET, "/api/reservations/**"), // 조회
                SecurityRequestMatcher.authenticatedOf(POST, "/api/reservations"), // 생성
                SecurityRequestMatcher.authenticatedOf(PATCH, "/api/reservations/{reservationId}/status"), // 수정
                SecurityRequestMatcher.authenticatedOf(PUT, "/api/reservations/{reservationId}"), // 수정
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, PROVIDER), DELETE, "/api/reservations/{reservationId}") // 삭제
        );
        matcherChain.addAll( // Review
                SecurityRequestMatcher.authenticatedOf(GET, "/api/reviews/**"), // 조회
                SecurityRequestMatcher.authenticatedOf(POST, "/api/reviews"), // 생성
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, CUSTOMER), PUT, "/api/reviews/{reviewId}"), // 수정
                SecurityRequestMatcher.hasAnyRolesOf(List.of(MASTER, CUSTOMER), DELETE, "/api/reviews/{reviewId}") // 삭제
        );
        matcherChain.addAll( // Report
                SecurityRequestMatcher.authenticatedOf(GET, "/api/reports/**"), // 조회
                SecurityRequestMatcher.authenticatedOf(POST, "/api/reports"), // 생성
                SecurityRequestMatcher.hasRoleOf(MASTER, DELETE, "/api/report/{reportId}") // 삭제
        );
        matcherChain.add( // Grade
                SecurityRequestMatcher.hasRoleOf(MASTER, "/api/grades/**") // FeignClient 조회
        );


        return matcherChain;
    }
}
