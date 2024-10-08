package com.tk.gg.security.hooks;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
public class SecurityRequestMatcherChain {
    private List<SecurityRequestMatcher> matchers = new ArrayList<>();

    public void add(SecurityRequestMatcher matcher) {
        matchers.add(matcher);
    }

    public void addAll(SecurityRequestMatcher... matcher) {
        matchers.addAll(List.of(matcher));
    }

    List<SecurityRequestMatcher> getMatchers() {
        return matchers;
    }
}
