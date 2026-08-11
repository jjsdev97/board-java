package com.project.board.boardapi.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_404", "게시글을 찾을 수 없습니다."),
    BOARD_ACCESS_DENIED(HttpStatus.FORBIDDEN, "BOARD_403", "게시글을 수정하거나 삭제할 권한이 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_404", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "MEMBER_409", "이미 사용 중인 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_401", "이메일 또는 비밀번호가 올바르지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_404", "댓글을 찾을 수 없습니다."),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMENT_403", "댓글을 수정하거나 삭제할 권한이 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status; this.code = code; this.message = message;
    }
    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}
