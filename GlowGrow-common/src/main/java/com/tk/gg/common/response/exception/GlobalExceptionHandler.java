package com.tk.gg.common.response.exception;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlowGlowException.class)
    public ResponseEntity<GlobalExceptionResponse> handleGlowGlowException(GlowGlowException e) {
        int statusCode = e.getError().getStatusCode();
        GlobalExceptionResponse response = ApiUtils.exception(statusCode, e.getError().getErrorCode(), e.getError().getMessage());

        return ResponseEntity.status(statusCode).body(response);
    }

}

