package com.tk.gg.common.response;

import org.springframework.http.HttpStatus;

public class ApiUtils {
    public static <T> GlobalResponse<T> makeResponse(int status, String message, T data) {
        return GlobalResponse.<T>builder()
                .code(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> GlobalResponse<T> success(String message, T data) {
        return makeResponse(HttpStatus.OK.value(), message, data);
    }

    public static GlobalResponse<String> success(String message) {
        return makeResponse(HttpStatus.OK.value(), message, null);
    }

    public static GlobalExceptionResponse exception(int status, String errorCode, String message) {
        return GlobalExceptionResponse.of(status, errorCode, message);
    }
}

