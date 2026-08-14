package com.project.board.boardapi.board.service;

import com.project.board.boardapi.board.cache.BoardCacheData;
import com.project.board.boardapi.board.cache.BoardPageCache;
import com.project.board.boardapi.board.domain.Board;
import com.project.board.boardapi.board.exception.BoardNotFoundException;
import com.project.board.boardapi.board.repository.BoardRepository;
import com.project.board.boardapi.common.constant.PageConstants;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardDataService {
    private final BoardRepository boardRepository;

    public BoardDataService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardDetails", key = "#boardId")
    public BoardCacheData getById(Long boardId) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
        return toCacheData(board);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardPages", key = "#page + ':' + #keyword")
    public BoardPageCache getPaged(int page, String keyword) {
        var pageable = PageRequest.of(page, PageConstants.DEFAULT_PAGE_SIZE, PageConstants.DEFAULT_SORT);
        Page<Board> boards = keyword.isEmpty()
                ? boardRepository.findAllByIsDeletedFalse(pageable)
                : boardRepository.findByTitleContainingAndIsDeletedFalse(keyword, pageable);

        List<BoardCacheData> content = new ArrayList<>(boards.getContent().stream()
                .map(this::toCacheData)
                .toList());

        return new BoardPageCache(content, boards.getNumber(), boards.getSize(), boards.getTotalElements());
    }

    private BoardCacheData toCacheData(Board board) {
        return new BoardCacheData(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getViewCount()
        );
    }
}
