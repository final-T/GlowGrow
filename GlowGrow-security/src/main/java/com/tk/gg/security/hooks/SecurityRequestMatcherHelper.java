package com.tk.gg.security.hooks;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityRequestMatcherHelper {
    private final SecurityRequestMatcherChain securityRequestMatcherChain;

    public AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry setAuthorizedRequest(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        for (SecurityRequestMatcher matcher: securityRequestMatcherChain.getMatchers())
            auth = toAuthorizedRequestFrom(auth, matcher);

        auth.anyRequest().permitAll();
        return auth;
    }

    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry toAuthorizedRequestFrom(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth, SecurityRequestMatcher matcher) {

        if (matcher.isAnyRequest()) {
            return setSecurityOptions(auth.anyRequest(), matcher);
        } else if (matcher.isUrlOnly()){
            return setSecurityOptions(auth.requestMatchers(matcher.getUrls()), matcher);
        } else {
            return setSecurityOptions(auth.requestMatchers(matcher.getHttpMethod(), matcher.getUrls()), matcher);
        }
    }

    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry setSecurityOptions(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authUrl, SecurityRequestMatcher matcher) {

        switch (matcher.getRequestHookType()) {
            case AUTHENTICATED:
                return authUrl.authenticated();
            case PERMIT_ALL:
                return authUrl.permitAll();
            case HAS_ANY_ROLE:
                return authUrl.hasAnyAuthority(matcher.getRoles());
            case HAS_ROLE:
                return authUrl.hasAuthority(matcher.getRole());
        }
        throw new RuntimeException("Unsupported request hook type: " + matcher.getRequestHookType());
    }
}