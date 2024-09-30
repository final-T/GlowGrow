package com.tk.gg.security.config;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.security.filter.JwtAuthorizationFilter;
import com.tk.gg.security.handlers.CustomAccessDeniedHandler;
import com.tk.gg.security.handlers.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GlowGrowSecurityConfiguration {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final SecurityRequestMatcherHelper requestMatcherHelper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(requestMatcherHelper::setAuthorizedRequest)
                .exceptionHandling(securityExceptionHandler->
                        securityExceptionHandler
                                .accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
