package com.project.board.boardapi.board.service;

import com.project.board.boardapi.board.cache.BoardCacheData;
import com.project.board.boardapi.board.cache.BoardPageCache;
import com.project.board.boardapi.board.domain.Board;
import com.project.board.boardapi.board.dto.BoardCreateRequest;
import com.project.board.boardapi.board.dto.BoardCreateResponse;
import com.project.board.boardapi.board.dto.BoardListResponse;
import com.project.board.boardapi.board.dto.BoardPageResponse;
import com.project.board.boardapi.board.dto.BoardResponse;
import com.project.board.boardapi.board.dto.BoardUpdateRequest;
import com.project.board.boardapi.board.exception.BoardAccessDeniedException;
import com.project.board.boardapi.board.exception.BoardNotFoundException;
import com.project.board.boardapi.board.repository.BoardRepository;
import com.project.board.boardapi.member.domain.Member;
import com.project.board.boardapi.member.exception.MemberNotFoundException;
import com.project.board.boardapi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardDataService boardDataService;
    private final BoardViewCountService viewCountService;

    public BoardService(BoardRepository boardRepository,
                        MemberRepository memberRepository,
                        BoardDataService boardDataService,
                        BoardViewCountService viewCountService) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.boardDataService = boardDataService;
        this.viewCountService = viewCountService;
    }

    @Transactional
    @CacheEvict(cacheNames = "boardPages", allEntries = true)
    public BoardCreateResponse create(Long memberId, BoardCreateRequest request) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Board saved = boardRepository.save(new Board(request.title(), request.content(), member));
        return new BoardCreateResponse(saved.getId());
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "boardDetails", key = "#boardId"),
            @CacheEvict(cacheNames = "boardPages", allEntries = true)
    })
    public BoardResponse update(Long boardId, Long memberId, BoardUpdateRequest request) {
        Board board = getBoard(boardId);
        checkOwner(board, memberId);
        if (request.title() != null) board.changeTitle(request.title());
        if (request.content() != null) board.changeContent(request.content());
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                viewCountService.get(board.getId(), board.getViewCount())
        );
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "boardDetails", key = "#boardId"),
            @CacheEvict(cacheNames = "boardPages", allEntries = true)
    })
    public void delete(Long boardId, Long memberId) {
        Board board = getBoard(boardId);
        checkOwner(board, memberId);
        board.softDelete(memberId);
        viewCountService.delete(boardId);
    }

    public BoardResponse getById(Long id) {
        BoardCacheData board = boardDataService.getById(id);
        long viewCount = viewCountService.increment(id, board.viewCount());
        return toResponse(board, viewCount);
    }

    public BoardPageResponse getPaged(int page, String keyword) {
        String normalizedKeyword = keyword == null || keyword.isBlank() ? "" : keyword;
        BoardPageCache cachedPage = boardDataService.getPaged(page, normalizedKeyword);

        List<BoardListResponse> content = cachedPage.content().stream()
                .map(board -> new BoardListResponse(board.id(), board.title(), board.content()))
                .toList();

        return BoardPageResponse.of(
                content,
                cachedPage.page(),
                cachedPage.size(),
                cachedPage.totalElements()
        );
    }

    private Board getBoard(Long id) {
        return boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
    }

    private void checkOwner(Board board, Long memberId) {
        if (!board.getMember().getId().equals(memberId)) throw new BoardAccessDeniedException();
    }

    private BoardResponse toResponse(BoardCacheData board, long viewCount) {
        return new BoardResponse(board.id(), board.title(), board.content(), viewCount);
    }
}
