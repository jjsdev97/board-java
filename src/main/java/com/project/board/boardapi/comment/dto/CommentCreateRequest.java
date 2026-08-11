package com.project.board.boardapi.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
    @NotNull(message = "게시글 번호가 포함되지 않았습니다.") Long boardId,
    @NotBlank(message = "내용을 입력해주세요.") String content
) {}
