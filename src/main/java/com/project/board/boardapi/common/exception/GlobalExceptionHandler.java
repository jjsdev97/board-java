package com.project.board.boardapi.common.exception;

import com.project.board.boardapi.auth.exception.LoginFailedException;
import com.project.board.boardapi.board.exception.BoardAccessDeniedException;
import com.project.board.boardapi.board.exception.BoardNotFoundException;
import com.project.board.boardapi.member.exception.DuplicateEmailException;
import com.project.board.boardapi.member.exception.MemberNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailed(LoginFailedException e){
        return buildDefaultResponseEntity(ErrorCode.LOGIN_FAILED);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoardNotFound(BoardNotFoundException e) {
        return buildDefaultResponseEntity(ErrorCode.BOARD_NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        String message = e.getBindingResult().getFieldErrors().isEmpty()
                ? errorCode.getMessage() : e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), message));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFound(MemberNotFoundException e) {
        return buildDefaultResponseEntity(ErrorCode.MEMBER_NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException e) {
        return buildDefaultResponseEntity(ErrorCode.DUPLICATE_EMAIL);
    }

    @ExceptionHandler(BoardAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleBoardAccessDenied(BoardAccessDeniedException e) {
        return buildDefaultResponseEntity(ErrorCode.BOARD_ACCESS_DENIED);
    }

    private ResponseEntity<ErrorResponse> buildDefaultResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.from(errorCode));
    }
}
