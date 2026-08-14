package com.project.board.boardapi.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BoardUpdateRequest(
        @Schema(description = "수정할 게시글 제목", example = "수정된 제목")
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다.")
        @Pattern(regexp = "(?s).*\\S.*", message = "제목을 입력해주세요.") String title,
        @Schema(description = "수정할 게시글 내용", example = "수정된 내용입니다.")
        @Pattern(regexp = "(?s).*\\S.*", message = "내용을 입력해주세요.") String content
) {
    @Schema(hidden = true)
    @AssertTrue(message = "제목과 내용 중 하나 이상을 입력해주세요.")
    public boolean isAnyFieldPresent() {
        return title != null || content != null;
    }
}
