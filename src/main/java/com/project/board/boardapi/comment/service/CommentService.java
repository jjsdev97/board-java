package com.project.board.boardapi.comment.service;

import com.project.board.boardapi.board.domain.Board;
import com.project.board.boardapi.board.dto.BoardCreateResponse;
import com.project.board.boardapi.board.exception.BoardAccessDeniedException;
import com.project.board.boardapi.board.exception.BoardNotFoundException;
import com.project.board.boardapi.board.repository.BoardRepository;
import com.project.board.boardapi.comment.domain.Comment;
import com.project.board.boardapi.comment.dto.CommentCreateRequest;
import com.project.board.boardapi.comment.dto.CommentCreateResponse;
import com.project.board.boardapi.comment.exception.CommentAccessDeniedException;
import com.project.board.boardapi.comment.exception.CommentNotFoundException;
import com.project.board.boardapi.comment.repository.CommentRepository;
import com.project.board.boardapi.member.domain.Member;
import com.project.board.boardapi.member.exception.MemberNotFoundException;
import com.project.board.boardapi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CommentService(MemberRepository memberRepository, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentCreateResponse create(Long memberId, Long boardId, CommentCreateRequest request) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        Comment saved = commentRepository.save(new Comment(request.content(), board, member));
        return new CommentCreateResponse(saved.getId());
    }

    @Transactional
    public void delete(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        this.checkOwner(comment, memberId);
        comment.softDelete(commentId);
    }

    private void checkOwner(Comment comment, Long memberId) {
        if (!comment.getMember().getId().equals(memberId)) throw new CommentAccessDeniedException();
    }
}
