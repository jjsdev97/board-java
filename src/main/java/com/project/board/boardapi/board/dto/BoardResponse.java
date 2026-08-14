package com.project.board.boardapi.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoardResponse(
        Long id,
        String title,
        String content,
        @Schema(description = "게시글 조회수", example = "10") long viewCount
) {}
