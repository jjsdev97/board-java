package com.project.board.boardapi.board.controller;

import com.project.board.boardapi.auth.security.LoginMember;
import com.project.board.boardapi.board.dto.*;
import com.project.board.boardapi.board.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    public BoardController(BoardService boardService) { this.boardService = boardService; }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BoardResponse>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(boardService.getPaged(page, keyword));
    }

    @PostMapping
    public ResponseEntity<BoardCreateResponse> create(
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody BoardCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.create(loginMember.memberId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody BoardUpdateRequest request) {
        return ResponseEntity.ok(boardService.update(id, loginMember.memberId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        boardService.delete(id, loginMember.memberId());
        return ResponseEntity.noContent().build();
    }
}
