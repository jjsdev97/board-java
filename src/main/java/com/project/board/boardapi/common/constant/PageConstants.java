package com.project.board.boardapi.common.constant;

import org.springframework.data.domain.Sort;

public final class PageConstants {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "id");
    private PageConstants() {}
}
