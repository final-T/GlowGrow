package com.tk.gg.common.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    CUSTOMER("CUSTOMER"),
    PROVIDER("PROVIDER"),
    MASTER("MASTER"),
    ;

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equalsIgnoreCase(role)) {
                return userRole;
            }
        }

        throw new IllegalArgumentException("No enum constant for authority: " + role);
    }
}
