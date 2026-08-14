package com.project.board.boardapi.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @Schema(description = "수정할 댓글 내용", example = "수정된 댓글입니다.")
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 200, message = "댓글은 200자 이하여야 합니다.") String content
) {
}
