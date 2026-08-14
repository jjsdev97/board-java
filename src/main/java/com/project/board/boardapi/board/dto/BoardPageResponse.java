package com.project.board.boardapi.board.dto;

import java.util.List;

public record BoardPageResponse(
        List<BoardListResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static BoardPageResponse of(
            List<BoardListResponse> content,
            int page,
            int size,
            long totalElements
    ) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return new BoardPageResponse(
                content,
                page,
                size,
                totalElements,
                totalPages,
                page == 0,
                totalPages == 0 || page >= totalPages - 1
        );
    }
}
