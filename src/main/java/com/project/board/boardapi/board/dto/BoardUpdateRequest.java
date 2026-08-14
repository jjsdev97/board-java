package com.project.board.boardapi.board.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BoardUpdateRequest(
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다.")
        @Pattern(regexp = "(?s).*\\S.*", message = "제목을 입력해주세요.") String title,
        @Pattern(regexp = "(?s).*\\S.*", message = "내용을 입력해주세요.") String content
) {
    @AssertTrue(message = "제목과 내용 중 하나 이상을 입력해주세요.")
    public boolean isAnyFieldPresent() {
        return title != null || content != null;
    }
}
