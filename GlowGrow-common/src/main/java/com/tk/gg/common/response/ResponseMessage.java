package com.tk.gg.common.response;

public enum ResponseMessage {
    // 게시판 관련 메시지
    POST_CREATE_SUCCESS("게시글이 성공적으로 생성되었습니다."),
    POST_UPDATE_SUCCESS("게시글이 성공적으로 수정되었습니다."),
    POST_DELETE_SUCCESS("게시글이 성공적으로 삭제되었습니다."),
    POST_RETRIEVE_SUCCESS("게시글을 성공적으로 조회했습니다."),

    // 좋아요 관련 메시지
    LIKE_SUCCESS("게시물에 좋아요를 표시했습니다."),
    UNLIKE_SUCCESS("게시물의 좋아요를 취소했습니다."),

    // 댓글 관련 메시지
    COMMENTS_WRITE_SUCCESS("댓글이 성공적으로 작성되었습니다."),
    COMMENTS_RETRIEVE_SUCCESS("댓글이 성공적으로 조회했습니다."),
    COMMENTS_UPDATE_SUCCESS("댓글이 성공적으로 수정되었습니다."),
    COMMENTS_DELETE_SUCCESS("댓글이 성공적으로 삭제되었습니다."),

    // 멀티미디어 관련 메시지
    MULTIMEDIA_UPLOAD_SUCCESS("파일 업로드가 성공적으로 수행되었습니다."), // S3
    MULTIMEDIA_RETRIEVE_SUCCESS("파일을 성공적으로 조회했습니다."),  // S3
    MULTIMEDIA_DELETE_SUCCESS("파일을 성공적으로 삭제했습니다."), // S3
    MULTIMEDIA_UPDATE_SUCCESS("멀티미디어를 성공적으로 수정했습니다."),

    // 결제 관련 메시지
    PAYMENT_RETRIEVE_SUCCESS("결제 정보가 성공적으로 조회되었습니다."),

    // 정산 관련 메시지
    SETTLEMENT_CREATE_SUCCESS("정산을 성공적으로 완료되었습니다."),
    SETTLEMENT_RETRIEVE_SUCCESS("정산을 성공적으로 조회되었습니다."),
    SETTLEMENT_UPDATE_SUCCESS("정산을 성공적으로 수정했습니다."),
    SETTLEMENT_DELETE_SUCCESS("정산을 성공적으로 삭제했습니다."),


    // 프로모션 관련 메시지
    PROMOTION_CREATE_SUCCESS("프로모션을 성공적으로 생성했습니다."),
    PROMOTION_UPDATE_SUCCESS("프로모션을 성공적으로 수정했습니다."),
    PROMOTION_DELETE_SUCCESS("프로모션을 성공적으로 삭제했습니다."),
    PROMOTION_RETRIEVE_SUCCESS("프로모션을 성공적으로 조회했습니다."),

    // 쿠폰 관련 메시지
    COUPON_CREATE_SUCCESS("쿠폰을 성공적으로 생성했습니다."),
    COUPON_UPDATE_SUCCESS("쿠폰을 성공적으로 수정했습니다."),
    COUPON_DELETE_SUCCESS("쿠폰을 성공적으로 삭제했습니다."),
    COUPON_RETRIEVE_SUCCESS("쿠폰을 성공적으로 조회했습니다."),
    COUPON_USER_CREATE_SUCCESS("사용자 쿠폰을 성공적으로 발급했습니다."),
    COUPON_USER_RETRIEVE_SUCCESS("사용자 쿠폰을 성공적으로 조회했습니다."),
    COUPON_USER_USE_SUCCESS("사용자 쿠폰을 성공적으로 사용했습니다."),

    // 인증 관련 메세지
    AUTH_SING_UP_SUCCESS("회원가입을 성공하였습니다."),
    AUTH_LOGIN_SUCCESS("로그인을 성공하였습니다."),

    // 예약 관련 메시지
    RESERVATION_CREATE_SUCCESS("예약을 성공적으로 생성했습니다."),
    RESERVATION_UPDATE_SUCCESS("예약 내용을 성공적으로 수정했습니다."),
    RESERVATION_UPDATE_STATUS_SUCCESS("예약 상태를 성공적으로 수정했습니다."),
    RESERVATION_DELETE_SUCCESS("예약을 성공적으로 삭제했습니다."),
    RESERVATION_RETRIEVE_SUCCESS("예약을 성공적으로 조회했습니다."),

    REVIEW_CREATE_SUCCESS("리뷰를 성공적으로 생성했습니다."),
    REVIEW_UPDATE_SUCCESS("리뷰 내용을 성공적으로 수정했습니다."),
    REVIEW_DELETE_SUCCESS("리뷰를 성공적으로 삭제했습니다."),
    REVIEW_RETRIEVE_SUCCESS("리뷰를 성공적으로 조회했습니다."),

    TIMESLOT_CREATE_SUCCESS("예약타임슬롯을 성공적으로 생성했습니다."),
    TIMESLOT_UPDATE_SUCCESS("예약타임슬롯 내용을 성공적으로 수정했습니다."),
    TIMESLOT_DELETE_SUCCESS("예약타임슬롯을 성공적으로 삭제했습니다."),
    TIMESLOT_RETRIEVE_SUCCESS("예약타임슬롯을 성공적으로 조회했습니다."),

    REPORT_CREATE_SUCCESS("신고를 성공적으로 생성했습니다."),
    REPORT_RETRIEVE_SUCCESS("신고를 성공적으로 생성했습니다."),
    REPORT_DELETE_SUCCESS("신고를 성공적으로 생성했습니다."),

    // 프로필 관련 메세지
    PROFILE_CREATE_SUCCESS("프로필을 성공적으로 생성했습니다."),
    PROFILE_UPDATE_SUCCESS("프로필을 내용을 성공적으로 수정했습니다."),
    PROFILE_DELETE_SUCCESS("프로필을 성공적으로 삭제했습니다."),
    PROFILE_RETRIEVE_SUCCESS("프로필을 성공적으로 조회했습니다."),

    // 평가 항목 관련 메시지
    GRADE_RETRIEVE_SUCCESS("평가정보를 성공적으로 조회했습니다."),

    // 유저 관련 메세지
    USER_CREATE_SUCCESS("사용자 정보를 성공적으로 생성했습니다."),
    USER_UPDATE_SUCCESS("사용자 정보를 성공적으로 수정했습니다."),
    USER_DELETE_SUCCESS("사용자 정보를 성공적으로 삭제했습니다."),
    USER_RETRIEVE_SUCCESS("사용자 정보를 성공적으로 조회했습니다."),

    // 등급 관련 메세지
    USER_GRADE_RETRIEVE_SUCCESS("사용자 정보를 성공적으로 조회했습니다."),

    // 알림 관련 메세지
    NOTIFICATION_SEND_SUCCESS("알림을 성공적으로 보냈습니다."),
    NOTIFICATION_READ_SUCCESS("알림을 성공적으로 읽었습니다.."),
    NOTIFICATION_DELETE_SUCCESS("알림을 성공적으로 삭제했습니다."),
    NOTIFICATION_RETRIEVE_SUCCESS("알림을 성공적으로 조회했습니다."),

    // 다른 메시지들...
    ;

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
