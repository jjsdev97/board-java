package com.project.board.boardapi.board.dto;

public record BoardListResponse(
        Long id,
        String title,
        String content
) {
}
