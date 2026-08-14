package com.project.board.boardapi.comment.service;

import com.project.board.boardapi.board.domain.Board;
import com.project.board.boardapi.board.exception.BoardNotFoundException;
import com.project.board.boardapi.board.repository.BoardRepository;
import com.project.board.boardapi.comment.domain.Comment;
import com.project.board.boardapi.comment.dto.CommentCreateRequest;
import com.project.board.boardapi.comment.dto.CommentCreateResponse;
import com.project.board.boardapi.comment.dto.CommentResponse;
import com.project.board.boardapi.comment.dto.CommentUpdateRequest;
import com.project.board.boardapi.comment.exception.CommentAccessDeniedException;
import com.project.board.boardapi.comment.exception.CommentNotFoundException;
import com.project.board.boardapi.comment.exception.InvalidCommentParentException;
import com.project.board.boardapi.comment.repository.CommentRepository;
import com.project.board.boardapi.member.domain.Member;
import com.project.board.boardapi.member.exception.MemberNotFoundException;
import com.project.board.boardapi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
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

        Comment comment = request.parentId() == null
                ? new Comment(request.content(), board, member)
                : createReply(request.content(), board, member, request.parentId());

        Comment saved = commentRepository.save(comment);
        return new CommentCreateResponse(saved.getId());
    }

    private Comment createReply(String content, Board board, Member member, Long parentId) {
        Comment parent = this.getComment(parentId);
        this.checkBoard(parent, board.getId());
        if (parent.getDepth() != 0) throw new InvalidCommentParentException();
        return new Comment(content, board, member, parent);
    }

    @Transactional
    public CommentResponse getById(Long boardId, Long commentId) {
        Comment comment = this.getComment(commentId);
        this.checkBoard(comment, boardId);
        return toResponse(comment);
    }

    @Transactional
    public List<CommentResponse> getByBoardId(Long boardId) {
        boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        return commentRepository.findAllByBoard_IdAndIsDeletedFalseOrderByIdAsc(boardId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long boardId, Long commentId, Long memberId) {
        Comment comment = this.getComment(commentId);
        this.checkBoard(comment, boardId);
        this.checkOwner(comment, memberId);
        comment.softDelete(memberId);
    }

    @Transactional
    public CommentResponse update(Long boardId, Long commentId, Long memberId, CommentUpdateRequest request) {
        Comment comment = this.getComment(commentId);
        this.checkBoard(comment, boardId);
        this.checkOwner(comment, memberId);

        comment.changeContent(request.content());

        return toResponse(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        Long parentId = comment.getParent() == null
                ? null
                : comment.getParent().getId();

        return new CommentResponse(
            comment.getId(),
            comment.getBoardId(),
            comment.getMember().getId(),
            comment.getMember().getName(),
            parentId,
            comment.getContent(),
            comment.getDepth()
        );
    }

    private void checkBoard(Comment comment, Long boardId) {
        if (!comment.getBoardId().equals(boardId)) throw new CommentNotFoundException(comment.getId());
    }

    private void checkOwner(Comment comment, Long memberId) {
        if (!comment.getMember().getId().equals(memberId)) throw new CommentAccessDeniedException();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }
}
