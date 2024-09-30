package com.tk.gg.common.response;

public enum ResponseMessage {
    POST_CREATE_SUCCESS("게시글이 성공적으로 생성되었습니다."),
    POST_UPDATE_SUCCESS("게시글이 성공적으로 수정되었습니다."),
    POST_DELETE_SUCCESS("게시글이 성공적으로 삭제되었습니다."),
    POST_RETRIEVE_SUCCESS("게시글을 성공적으로 조회했습니다.");
    // 다른 메시지들...

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
