package com.project.board.boardapi.board.service;

import com.project.board.boardapi.board.domain.Board;
import com.project.board.boardapi.board.dto.BoardCreateRequest;
import com.project.board.boardapi.board.dto.BoardCreateResponse;
import com.project.board.boardapi.board.dto.BoardResponse;
import com.project.board.boardapi.board.dto.BoardUpdateRequest;
import com.project.board.boardapi.board.exception.BoardAccessDeniedException;
import com.project.board.boardapi.board.exception.BoardNotFoundException;
import com.project.board.boardapi.board.repository.BoardRepository;
import com.project.board.boardapi.common.constant.PageConstants;
import com.project.board.boardapi.member.domain.Member;
import com.project.board.boardapi.member.exception.MemberNotFoundException;
import com.project.board.boardapi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public BoardCreateResponse create(Long memberId, BoardCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Board saved = boardRepository.save(new Board(request.title(), request.content(), member));
        return new BoardCreateResponse(saved.getId());
    }

    @Transactional
    public BoardResponse update(Long boardId, Long memberId, BoardUpdateRequest request) {
        Board board = getBoard(boardId);
        checkOwner(board, memberId);
        if (request.title() != null) board.changeTitle(request.title());
        if (request.content() != null) board.changeContent(request.content());
        return toResponse(board);
    }

    @Transactional
    public void delete(Long boardId, Long memberId) {
        Board board = getBoard(boardId);
        checkOwner(board, memberId);
        board.softDelete(memberId);
    }

    public BoardResponse getById(Long id) {
        return toResponse(getBoard(id));
    }

    public Page<BoardResponse> getPaged(int page, String keyword) {
        var pageable = PageRequest.of(page, PageConstants.DEFAULT_PAGE_SIZE, PageConstants.DEFAULT_SORT);
        Page<Board> boards = keyword == null || keyword.isBlank()
                ? boardRepository.findAll(pageable)
                : boardRepository.findByTitleContaining(keyword, pageable);
        return boards.map(this::toResponse);
    }

    private Board getBoard(Long id) {
        return boardRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
    }

    private void checkOwner(Board board, Long memberId) {
        if (!board.getMember().getId().equals(memberId)) throw new BoardAccessDeniedException();
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getContent());
    }
}
