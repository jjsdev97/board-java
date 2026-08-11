package com.project.board.boardapi.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("댓글(id=" + id + ")를 찾을 수 없습니다.");
    }
}
