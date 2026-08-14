package com.project.board.boardapi.board.cache;

import java.io.Serializable;
import java.util.List;

public record BoardPageCache(
        List<BoardCacheData> content,
        int page,
        int size,
        long totalElements
) implements Serializable {
}
