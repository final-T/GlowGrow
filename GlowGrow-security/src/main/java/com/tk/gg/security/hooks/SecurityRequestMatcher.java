package com.tk.gg.security.hooks;

import com.tk.gg.common.enums.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SecurityRequestMatcher {
    @Getter
    private RequestHookType requestHookType;
    @Getter
    private String[] urls;
    @Getter
    private HttpMethod httpMethod;
    private List<UserRole> roles = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private SecurityRequestMatcher(RequestHookType requestHookType, String[] urls, HttpMethod httpMethod, List<UserRole> roles) {
        this.requestHookType = requestHookType;
        this.urls = urls;
        this.httpMethod = httpMethod;
        this.roles = roles;
    }

    public boolean isAnyRequest() {
        return urls == null && httpMethod == null;
    }

    public boolean isUrlOnly() {
        return httpMethod == null;
    }

    public static SecurityRequestMatcher authenticatedOf(String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.AUTHENTICATED)
            .urls(urls)
            .build();
    }

    public static SecurityRequestMatcher authenticatedOf(HttpMethod method, String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.AUTHENTICATED)
            .httpMethod(method)
            .urls(urls)
            .build();
    }

    public static SecurityRequestMatcher anyAuthenticated() {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.AUTHENTICATED)
            .build();
    }

    public static SecurityRequestMatcher permitAllOf(String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.PERMIT_ALL)
            .urls(urls)
            .build();
    }

    public static SecurityRequestMatcher permitAllOf(HttpMethod method, String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.PERMIT_ALL)
            .httpMethod(method)
            .urls(urls)
            .build();
    }

    public static SecurityRequestMatcher anyPermitAll() {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.PERMIT_ALL)
            .build();
    }

    public static SecurityRequestMatcher hasRoleOf(UserRole role, String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.HAS_ROLE)
            .urls(urls)
            .roles(Collections.singletonList(role))
            .build();
    }

    public static SecurityRequestMatcher hasRoleOf(UserRole role, HttpMethod method, String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.HAS_ROLE)
            .httpMethod(method)
            .roles(Collections.singletonList(role))
            .urls(urls)
            .build();
    }

    public static SecurityRequestMatcher anyHasRole(UserRole role) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.HAS_ROLE)
            .roles(Collections.singletonList(role))
            .build();
    }

    public static SecurityRequestMatcher hasAnyRolesOf(List<UserRole> roles, String ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.HAS_ANY_ROLE)
            .urls(urls)
            .roles(roles)
            .build();
    }

    public static SecurityRequestMatcher hasAnyRolesOf(List<UserRole> roles, HttpMethod method, String  ...urls) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.HAS_ANY_ROLE)
            .urls(urls)
            .httpMethod(method)
            .roles(roles)
            .build();
    }

    public static SecurityRequestMatcher hasAnyRolesOf(List<UserRole> roles) {
        return SecurityRequestMatcher.builder()
            .requestHookType(RequestHookType.HAS_ANY_ROLE)
            .roles(roles)
            .build();
    }

    public String getRole() {
        return roles.get(0).name();
    }

    public String[] getRoles() {
        return roles.stream()
            .map(UserRole::name)
            .toArray(String[]::new);
    }

    enum RequestHookType {
        AUTHENTICATED,
        HAS_ROLE,
        HAS_ANY_ROLE,
        PERMIT_ALL
    }
}
