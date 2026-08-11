package com.project.board.boardapi.member.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("사용자(id=" + id + ")를 찾을 수 없습니다.");
    }
}
