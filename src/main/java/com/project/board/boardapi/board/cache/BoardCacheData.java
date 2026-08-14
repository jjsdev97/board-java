package com.project.board.boardapi.board.cache;

import java.io.Serializable;

public record BoardCacheData(
        Long id,
        String title,
        String content,
        long viewCount
) implements Serializable {
}
