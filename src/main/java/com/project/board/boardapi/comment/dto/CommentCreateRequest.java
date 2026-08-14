package com.project.board.boardapi.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
    @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
    @NotBlank(message = "내용을 입력해주세요.") String content,

    @Schema(description = "부모 댓글 ID. 일반 댓글이면 생략", example = "1")
    Long parentId
) {}
