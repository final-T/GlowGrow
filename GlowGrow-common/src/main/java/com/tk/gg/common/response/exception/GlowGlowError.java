package com.tk.gg.common.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlowGlowError {
    //공통
    NO_SEARCH_RESULTS(404,"SEARCH_001","검색 결과가 없습니다."),


    // Auth (인증 관련 에러)
    AUTH_INVALID_CREDENTIALS(404, "AUTH_001", "유효하지 않은 자격 증명입니다."),
    AUTH_UNAUTHORIZED(404, "AUTH_002", "인증되지 않은 사용자입니다."),
    AUTH_TOKEN_EXPIRED(404, "AUTH_003", "토큰이 만료되었습니다."),


    // Post (게시판 관련 에러)
    POST_NO_EXIST(404,"POST_001","존재하지 않은 게시물입니다."),
    POST_LIKE_UPDATE_FAILED(404, "LIKE_001", "게시물 좋아요 업데이트에 실패했습니다."),
    COMMENT_NO_EXIST(404,"COMMENT_001","존재하지 않은 댓글입니다."),
    COMMENT_DELETED(404,"COMMENT_002","삭제된 댓글에는 답글을 작성할 수 없습니다."),
    COMMENT_DEPTH_EXCEEDED(400,"COMMENT_003","댓글의 깊이 제한을 초과했습니다. 대댓글은 1단계까지만 허용됩니다."),


    // 프로모션 관련 에러
    PROMOTION_NO_EXIST(404, "PROMOTION_001", "존재하지 않은 프로모션입니다.")
    ,

    // 예약 및 리뷰 관련 에러
    TIMESLOT_ALREADY_EXIST(404, "TIMESLOT_001","예약타임슬롯 생성에 실패했습니다."),
    TIMESLOT_NO_EXIST(404, "TIMESLOT_002","존재하지 않는 예약타임슬롯 정보 입니다."),
    RESERVATION_CREATE_FAILED(404, "RESERVATION_001", "예약을 생성에 실패했습니다."),
    RESERVATION_NO_EXIST(404, "RESERVATION_002","존재하지 않는 예약 정보입니다."),
    RESERVATION_UPDATE_FAILED(400, "RESERVATION_003", "예약 내용 수정에 실패했습니다."),
    REVIEW_CANNOT_FAILED(404, "REVIEW_001", "예약에 대한 리뷰 생성에 실패했습니다."),
    REVIEW_NO_EXIST(404, "REVIEW_002", "존재하지 않는 리뷰입니다.");

    private final int statusCode; // HTTP 상태 코드
    private final String errorCode; // 내부 시스템의 에러 코드
    private final String message; // 에러 메시지
}

