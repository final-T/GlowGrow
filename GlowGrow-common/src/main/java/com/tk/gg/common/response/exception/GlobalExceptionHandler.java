package com.tk.gg.common.response.exception;

import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlowGlowException.class)
    public ResponseEntity<GlobalExceptionResponse> handleGlowGlowException(GlowGlowException e) {
        int statusCode = e.getError().getStatusCode();
        GlobalExceptionResponse response = ApiUtils.exception(statusCode, e.getError().getErrorCode(), e.getError().getMessage());

        return ResponseEntity.status(statusCode).body(response);
    }

    //Request Validation 에 의한 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalExceptionResponse> ExceptionHandler(MethodArgumentNotValidException e) {

        GlobalExceptionResponse errorResponse = ApiUtils.exception(
                HttpStatus.BAD_REQUEST.value(),
                "REQ_VALIDATION",
                "잘못된 요청입니다."
        );
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

