package com.tk.gg.common.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");


    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public static Gender fromString(String name) {
        for (Gender gender : Gender.values()) {
            if (gender.getName().equalsIgnoreCase(name)) {
                return gender;
            }
        }

        throw new IllegalArgumentException("No enum constant for authority: " + name);
    }
}
