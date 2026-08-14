package com.project.board.boardapi.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 200, message = "댓글은 200자 이하여야 합니다.") String content
) {
}
