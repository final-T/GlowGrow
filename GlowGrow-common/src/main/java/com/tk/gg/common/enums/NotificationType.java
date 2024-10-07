package com.tk.gg.common.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    PROMOTION("PROMOTION"),
    RESERVATION("RESERVATION"),
    PAYMENT("PAYMENT"),
    POST("POST"),
    CHAT("CHAT")
    ;

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }

    public static NotificationType fromString(String name) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.getName().equalsIgnoreCase(name)) {
                return notificationType;
            }
        }

        throw new IllegalArgumentException("No enum constant for authority: " + name);
    }
}
