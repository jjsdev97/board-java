package com.project.board.boardapi.comment.exception;

public class InvalidCommentParentException extends RuntimeException {
    public InvalidCommentParentException() {
        super("대댓글에는 답글을 작성할 수 없습니다.");
    }
}
