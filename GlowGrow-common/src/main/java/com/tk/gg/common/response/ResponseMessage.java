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


    // 프로모션 관련 메시지
    PROMOTION_CREATE_SUCCESS("프로모션을 성공적으로 생성했습니다."),
    PROMOTION_UPDATE_SUCCESS("프로모션을 성공적으로 수정했습니다."),
    PROMOTION_DELETE_SUCCESS("프로모션을 성공적으로 삭제했습니다."),
    PROMOTION_RETRIEVE_SUCCESS("프로모션을 성공적으로 조회했습니다."),

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
    TIMESLOT_RETRIEVE_SUCCESS("예약타임슬롯을 성공적으로 조회했습니다.")
    ;

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
