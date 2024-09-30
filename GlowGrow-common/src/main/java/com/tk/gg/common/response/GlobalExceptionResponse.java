package com.tk.gg.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GlobalExceptionResponse<T> {
    private int status;
    private String errorCode;
    private String message;

    public static GlobalExceptionResponse<String> of(int status, String errorCode, String message) {
        return new GlobalExceptionResponse<>(status, errorCode, message);
    }
}

