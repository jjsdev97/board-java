package com.project.board.boardapi.comment.controller;

import com.project.board.boardapi.auth.security.LoginMember;
import com.project.board.boardapi.comment.service.CommentService;
import com.project.board.boardapi.comment.dto.CommentCreateRequest;
import com.project.board.boardapi.comment.dto.CommentCreateResponse;
import jakarta.validation.Valid;
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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        commentService.delete(id, loginMember.memberId());
        return ResponseEntity.noContent().build();
    }
}
