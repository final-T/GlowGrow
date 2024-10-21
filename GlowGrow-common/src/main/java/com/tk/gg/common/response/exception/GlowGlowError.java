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
    AUTH_ALREADY_SING_UP(409, "AUTH_004", "이미 회원가입이 되어있는 email 입니다."),


    // Post (게시판 관련 에러)
    POST_NO_EXIST(404,"POST_001","존재하지 않은 게시물입니다."),
    POST_NO_AUTH_PERMISSION_DENIED(403,"POST_002","게시물에 대한 권한이 없습니다."),
    POST_FILE_NO_EXIST(404, "POST_FILE_001", "존재하지 않는 게시물 파일입니다."),


    // Like (좋아요 관련 에러)
    POST_LIKE_UPDATE_FAILED(404, "LIKE_001", "게시물 좋아요 업데이트에 실패했습니다."),

    // Comment (댓글 관련 에러)
    COMMENT_NO_EXIST(404,"COMMENT_001","존재하지 않은 댓글입니다."),
    COMMENT_DELETED(404,"COMMENT_002","삭제된 댓글에는 답글을 작성할 수 없습니다."),
    COMMENT_DEPTH_EXCEEDED(400,"COMMENT_003","댓글의 깊이 제한을 초과했습니다. 대댓글은 1단계까지만 허용됩니다."),
    COMMENT_NO_AUTH_PERMISSION_DENIED(403,"COMMENT_004","댓글에 대한 권한이 없습니다."),

    // Payment (결제 관련 에러)
    PAYMENT_NO_EXIST(404,"PAYMENT_001","존재하지 않은 결제정보입니다."),
    PAYMENT_AMOUNT_MISMATCH(404,"PAYMENT_002","결제 금액이 일치하지 않습니다."),
    ALREADY_APPROVED(404,"PAYMENT_003","이미 승인된 결제입니다."),
    MY_PAYMENT_NOT_FOUND(404,"PAYMENT_004","내 결제정보가 존재하지 않습니다."),
    PAYMENT_REQUEST_NOT_FOUND(404,"PAYMENT_005","결제 요청이 존재하지 않습니다."),
    PAYMENT_NO_AUTH_PERMISSION_DENIED(403,"PAYMENT_006","결제 정보에 대한 권한이 없습니다."),

    // Refund (환불 관련 에러)
    REFUND_PROCESS_FAILED(404,"REFUND_001","환불 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    PAYMENT_CANCELED_OR_REFUNDED(404,"REFUND_002","이미 취소 또는 환불된 결제입니다."),
    PAYMENT_ALREADY_SETTLEMENT(404,"REFUND_003","이미 정산이 완료된 결제입니다."),

    // Settlement (정산 관련 에러)
    SETTLEMENT_NO_EXIST(404,"SETTLEMENT_001","존재하지 않은 정산입니다."),
    SETTLEMENT_NO_AUTH_PERMISSION_DENIED(403,"SETTLEMENT_002","정산에 대한 권한이 없습니다."),
    SETTLEMENT_SELF_ACCESS_ONLY(403,"SETTLEMENT_003","자신의 정산 정보만 확인할 수 있습니다."),
    SETTLEMENT_NO_PAYMENTS_FOUND(404, "SETTLEMENT_004", "해당 기간에 정산 가능한 결제 정보가 존재하지 않습니다."),
    SETTLEMENT_ALREADY_EXISTS(409, "SETTLEMENT_005", "이미 정산이 완료된 데이터입니다."),
    SETTLEMENT_NO_PROVIDER_ID_FOR_PROVIDER(400, "SETTLEMENT_004", "providerId가 누락되었습니다. PROVIDER 역할일 경우 필수 입력 사항입니다."),




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
    TIMESLOT_NOT_OWNER(404, "TIMESLOT_003","해당 예약타임슬롯에 대한 권한이 없습니다."),
    RESERVATION_CREATE_FAILED(404, "RESERVATION_001", "예약 생성에 실패했습니다."),
    RESERVATION_NO_EXIST(404, "RESERVATION_002","존재하지 않는 예약 정보입니다."),
    RESERVATION_UPDATE_FAILED(400, "RESERVATION_003", "예약 내용 수정에 실패했습니다."),
    RESERVATION_DELETE_FAILED(400, "RESERVATION_004", "예약 내용 삭제에 실패했습니다."),
    RESERVATION_NOT_OWNER(400, "RESERVATION_005", "해당 예약에 대한 권한이 없습니다."),
    RESERVATION_FORBIDDEN_STATUS(400, "RESERVATION_006", "사용자가 수정할 수 있는 예약 상태가 아닙니다."),
    RESERVATION_ALREADY_EXIST(400, "RESERVATION_007", "이미 해당 시간에 예약이 있습니다."),
    RESERVATION_NOT_DONE_FOR_PAYMENT(404, "REVIEW_008", "서비스 완료된 예약만 결제 요청을 할 수 있습니다."),
    RESERVATION_BEFORE_NOW(404, "RESERVATION_009","현재 시간 이전의 예약은 불가합니다."),
    RESERVATION_WRONG_TIME(404, "RESERVATION_010","잘못된 요청 예약 날짜 및 시간입니다."),
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
    PROFILE_ALREADY_EXIST(409, "PROFILE_012","이미 프로필이 존재합니다."),

    //평가항목 관련 에러
    GRADE_NO_EXIST(404, "GRADE_001", "존재하지 않는 평가정보 입니다."),
    GRADE_INVALID_ROLES(401, "GRADE_002","해당 평가정보에 대한 권한이 업습니다."),

    // 회원 등급 관련 에러
    USER_GRADE_NOT_AVAILABLE(404, "USER_GRADE_001", "알맞은 등급이 아닙니다."),
    USER_GRADE_NO_EXIST(404, "USER_GRADE_002", "사용자의 등급이 없습니다."),
    GRADE_NOT_NO_EXIST(404, "USER_GRADE_003", "해당 등급이 존재하지 않습니다."),

    // 알림 관련 에러
    NOTIFICATION_NO_EXIST(404, "NOTIFICATION_001", "해당 알림이 존재하지 않습니다."),
    NOTIFICATION_ALREADY_READ(409, "NOTIFICATION_002", "해당 알림은 이미 읽음 처리 되어있습니다."),

    // 파일 관련 에러
    MULTIMEDIA_REQUEST_PARAM_NULL(409, "MULTIMEDIA_001", "필수 요청값이 없습니다."),
    MULTIMEDIA_FILE_SIZE_INVALID(409, "MULTIMEDIA_002", "파일 크기가 너무 큽니다."),
    MULTIMEDIA_FILE_EXTENSION_INVALID(409, "MULTIMEDIA_003", "지원하지 않는 파일 형식입니다."),
    MULTIMEDIA_FILE_INVALID(409, "MULTIMEDIA_004", "제목이 없는 파일입니다."),
    MULTIMEDIA_UPLOAD_FAIL(409, "MULTIMEDIA_005", "파일을 업로드 하는데 실패했습니다."),
    MULTIMEDIA_NO_EXIST(404, "MULTIMEDIA_006", "존재하지 않는 파일입니다."),

    ;
    private final int statusCode; // HTTP 상태 코드
    private final String errorCode; // 내부 시스템의 에러 코드
    private final String message; // 에러 메시지
}

