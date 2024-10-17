package com.tk.gg.common.response.exception;

import lombok.Getter;

@Getter
public class GlowGlowException extends RuntimeException {
    private final GlowGlowError error;

    public GlowGlowException(GlowGlowError error){
        super(error.getMessage()); // 메시지 설정
        this.error = error;
    }
}

