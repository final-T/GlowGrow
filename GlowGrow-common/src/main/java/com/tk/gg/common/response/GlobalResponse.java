package com.tk.gg.common.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalResponse<T> {
    private int code;
    private String message;
    private T data;

    @Builder(access = AccessLevel.MODULE)
    private GlobalResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
