package com.tk.gg.common.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlowGlowError {

    // Auth (인증 관련 에러)
    AUTH_INVALID_CREDENTIALS(404, "AUTH_001", "유효하지 않은 자격 증명입니다."),
    AUTH_UNAUTHORIZED(404, "AUTH_002", "인증되지 않은 사용자입니다."),
    AUTH_TOKEN_EXPIRED(404, "AUTH_003", "토큰이 만료되었습니다."),


    // Post (게시판 관련 에러)
    POST_NO_EXIST(404,"POST_001","존재하지 않은 게시물입니다."),

    // 프로모션 관련 에러
    PROMOTION_NO_EXIST(404, "PROMOTION_001", "존재하지 않은 프로모션입니다.")
    ;

    private final int statusCode; // HTTP 상태 코드
    private final String errorCode; // 내부 시스템의 에러 코드
    private final String message; // 에러 메시지
}

