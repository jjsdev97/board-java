package com.project.board.boardapi.comment.dto;

public record CommentResponse(
        Long id,
        Long boardId,
        Long memberId,
        String memberName,
        Long parentId,
        String content,
        int depth
) {
}