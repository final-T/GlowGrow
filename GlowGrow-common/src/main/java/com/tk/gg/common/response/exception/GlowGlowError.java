package com.tk.gg.common.response.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlowGlowError {
    //공통
    NO_SEARCH_RESULTS(404,"SEARCH_001","검색 결과가 없습니다."),
    USER_NO_EXIST(404,"USER_001","존재하지 않은 사용자입니다."),


    // Auth (인증 관련 에러)
    AUTH_INVALID_CREDENTIALS(404, "AUTH_001", "유효하지 않은 자격 증명입니다."),
    AUTH_UNAUTHORIZED(404, "AUTH_002", "인증되지 않은 사용자입니다."),
    AUTH_TOKEN_EXPIRED(404, "AUTH_003", "토큰이 만료되었습니다."),


    // Post (게시판 관련 에러)
    POST_NO_EXIST(404,"POST_001","존재하지 않은 게시물입니다."),
    POST_NO_AUTH_PERMISSION_DENIED(403,"POST_002","게시물에 대한 권한이 없습니다."),

    // Like (좋아요 관련 에러)
    POST_LIKE_UPDATE_FAILED(404, "LIKE_001", "게시물 좋아요 업데이트에 실패했습니다."),

    // Comment (댓글 관련 에러)
    COMMENT_NO_EXIST(404,"COMMENT_001","존재하지 않은 댓글입니다."),
    COMMENT_DELETED(404,"COMMENT_002","삭제된 댓글에는 답글을 작성할 수 없습니다."),
    COMMENT_DEPTH_EXCEEDED(400,"COMMENT_003","댓글의 깊이 제한을 초과했습니다. 대댓글은 1단계까지만 허용됩니다."),
    COMMENT_NO_AUTH_PERMISSION_DENIED(403,"COMMENT_004","댓글에 대한 권한이 없습니다."),

    // Multimedia (멀티미디어 관련 에러)
    MULTIMEDIA_NO_EXIST(404,"MULTIMEDIA_001","존재하지 않은 파일입니다."),
    INVALID_FILE_NAME(404, "MULTIMEDIA_002","파일 이름이 유효하지 않습니다."),
    UNSUPPORTED_FILE_EXTENSION(404,"MULTIMEDIA_003","지원하지 않는 확장자입니다."),
    IMAGE_FILE_SIZE_EXCEEDED(404,"MULTIMEDIA_004","이미지 파일 크기가 허용된 최대 크기를 초과했습니다."),
    VIDEO_FILE_SIZE_EXCEEDED(404,"MULTIMEDIA_005","동영상 파일 크기가 허용된 최대 크기를 초과했습니다."),

    // Payment (결제 관련 에러)
    PAYMENT_NO_EXIST(404,"PAYMENT_001","존재하지 않은 결제정보입니다."),
    PAYMENT_AMOUNT_MISMATCH(404,"PAYMENT_002","결제 금액이 일치하지 않습니다."),
    ALREADY_APPROVED(404,"PAYMENT_003","이미 승인된 결제입니다."),

    // Refund (환불 관련 에러)
    REFUND_PROCESS_FAILED(404,"REFUND_001","환불 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    PAYMENT_ALREADY_CANCELED(404,"REFUND_002","이미 취소된 결제입니다."),



    // 프로모션 관련 에러
    PROMOTION_NO_EXIST(404, "PROMOTION_001", "존재하지 않은 프로모션입니다."),

    // Coupon (쿠폰 관련 에러)
    COUPON_NO_EXIST(404, "COUPON_001", "존재하지 않은 쿠폰입니다."),
    COUPON_ALREADY_ISSUED(409, "COUPON_002", "이미 발급된 쿠폰입니다."),
    COUPON_ALREADY_USED(409, "COUPON_003", "이미 사용된 쿠폰입니다."),
    COUPON_EXPIRED(409, "COUPON_004", "만료된 쿠폰입니다."),
    COUPON_QUANTITY_EXCEEDED(409, "COUPON_005", "쿠폰 발급 수량을 초과했습니다."),

    // 예약 및 리뷰 관련 에러
    TIMESLOT_ALREADY_EXIST(404, "TIMESLOT_001","예약타임슬롯 생성에 실패했습니다."),
    TIMESLOT_NO_EXIST(404, "TIMESLOT_002","존재하지 않는 예약타임슬롯 정보 입니다."),
    RESERVATION_CREATE_FAILED(404, "RESERVATION_001", "예약을 생성에 실패했습니다."),
    RESERVATION_NO_EXIST(404, "RESERVATION_002","존재하지 않는 예약 정보입니다."),
    RESERVATION_UPDATE_FAILED(400, "RESERVATION_003", "예약 내용 수정에 실패했습니다."),
    RESERVATION_DELETE_FAILED(400, "RESERVATION_004", "예약 내용 삭제에 실패했습니다."),
    RESERVATION_NOT_OWNER(400, "RESERVATION_005", "해당 예약에 대한 권한이 없습니다."),
    REVIEW_CREATE_FAILED(404, "REVIEW_001", "예약에 대한 리뷰 생성에 실패했습니다."),
    REVIEW_NO_EXIST(404, "REVIEW_002", "존재하지 않는 리뷰입니다."),
    REVIEW_WRONG_REVIEWER_ID(404, "REVIEW_003","요청 리뷰어와 현재 사용자가 일치하지 않습니다."),
    REVIEW_NOT_OWNER(404,"REVIEW_004","해당 리뷰에 대한 권한이 없습니다."),

    // 신고 관련 에러
    REPORT_NOT_OWNER(404, "REPORT_001", "해당 신고에 대한 권한이 없습니다."),
    REPORT_NO_EXIST(404, "REPORT_002","존재하지 않는 신고 정보 입니다."),
    REPORT_ALREADY_EXIST(404, "REPORT_003", "이미 신고된 예약입니다."),

    // 프로필 관련 에러
    PROFILE_NO_EXIST(404, "PROFILE_001", "존재하지 않는 프로필입니다."),
    AWARD_NO_EXIST(404, "PROFILE_002", "존재하지 않는 수상경력입니다.."),
    PREFER_PRICE_NO_EXIST(404, "PROFILE_003", "존재하지 않는 선호 가격입니다."),
    PREFER_LOCATION_NO_EXIST(404, "PROFILE_004", "존재하지 않는 선호 지역입니다."),
    PREFER_STYLE_NO_EXIST(404, "PROFILE_005", "존재하지 않는 선호 스타일입니다."),
    WORK_EXPERIENCE_NO_EXIST(404, "PROFILE_006", "존재하지 않는 경력입니다."),
    AWARD_ALREADY_EXIST(409, "PROFILE_007", "이미 존재하는 수상 경력입니다."),
    PREFER_LOCATION_ALREADY_EXIST(409, "PROFILE_008", "이미 존재하는 선호 지역입니다."),
    PREFER_PRICE_ALREADY_EXIST(409, "PROFILE_009", "이미 존재하는 선호 가격입니다."),
    PREFER_STYLE_ALREADY_EXIST(409, "PROFILE_010", "이미 존재하는 선호 스타일입니다."),
    WORK_EXPERIENCE_ALREADY_EXIST(409, "PROFILE_011", "이미 존재하는 경력입니다."),

    //평가항목 관련 에러
    GRADE_NO_EXIST(404, "GRADE_001", "존재하지 않는 평가정보 입니다."),
    GRADE_INVALID_ROLES(401, "GRADE_002","해당 평가정보에 대한 권한이 업습니다."),

    // 회원 등급 관련 에러
    USER_GRADE_NOT_AVAILABLE(404, "USER_GRADE_001", "알맞은 등급이 아닙니다."),
    USER_GRADE_NO_EXIST(404, "USER_GRADE_002", "사용자의 등급이 없습니다."),
    GRADE_NOT_NO_EXIST(404, "USER_GRADE_003", "해당 등급이 존재하지 않습니다."),

    NOTIFICATION_NO_EXIST(404, "NOTIFICATION_001", "해당 알림이 존재하지 않습니다."),
    NOTIFICATION_ALREADY_READ(409, "NOTIFICATION_002", "해당 알림은 이미 읽음 처리 되어있습니다."),
    ;
    private final int statusCode; // HTTP 상태 코드
    private final String errorCode; // 내부 시스템의 에러 코드
    private final String message; // 에러 메시지
}

