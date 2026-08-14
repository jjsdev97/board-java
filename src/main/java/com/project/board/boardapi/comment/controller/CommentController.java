package com.project.board.boardapi.comment.controller;

import com.project.board.boardapi.auth.security.LoginMember;
import com.project.board.boardapi.comment.dto.CommentResponse;
import com.project.board.boardapi.comment.dto.CommentUpdateRequest;
import com.project.board.boardapi.comment.service.CommentService;
import com.project.board.boardapi.comment.dto.CommentCreateRequest;
import com.project.board.boardapi.comment.dto.CommentCreateResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentCreateResponse> create(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.create(loginMember.memberId(), boardId, request));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable Long boardId,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(boardId, id));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getByBoardId(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getByBoardId(boardId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long boardId,
                                       @PathVariable Long id,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        commentService.delete(boardId, id, loginMember.memberId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long boardId,
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody CommentUpdateRequest request
    ){
        return ResponseEntity.ok(commentService.update(boardId, id, loginMember.memberId(), request));
    }
}
