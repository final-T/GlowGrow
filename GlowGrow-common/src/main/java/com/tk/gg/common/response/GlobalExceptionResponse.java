package com.tk.gg.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GlobalExceptionResponse {
    private int status;
    private String errorCode;
    private String message;
    private Map<String, String> validation;

    public void addValidation(String field, String errorMessage) {
        if (validation == null) {
            validation = new HashMap<>();
        }
        this.validation.put(field, errorMessage);
    }

    public static GlobalExceptionResponse of(int status, String errorCode, String message) {
        return new GlobalExceptionResponse(status, errorCode, message, null);
    }
}

