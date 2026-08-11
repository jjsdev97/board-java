package com.project.board.boardapi.board.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(Long id) {
        super("게시글(id=" + id + ")를 찾을 수 없습니다.");
    }
}
