package com.project.board.boardapi.board.dto;

import jakarta.validation.constraints.Size;

public record BoardUpdateRequest(
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다.") String title,
        String content
) {}
