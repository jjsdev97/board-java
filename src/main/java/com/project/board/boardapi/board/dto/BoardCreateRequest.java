package com.project.board.boardapi.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateRequest(
        @Schema(description = "게시글 제목", example = "첫 번째 게시글")
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 100, message = "제목은 100자 이하여야 합니다.") String title,
        @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
        @NotBlank(message = "내용을 입력해주세요.") String content
) {}
