package com.project.board.boardapi.board.controller;

import com.project.board.boardapi.auth.security.LoginMember;
import com.project.board.boardapi.board.dto.*;
import com.project.board.boardapi.board.service.BoardService;
import com.project.board.boardapi.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@Tag(name = "게시글", description = "게시글 생성, 조회, 수정 및 삭제 API")
public class BoardController {
    private final BoardService boardService;
    public BoardController(BoardService boardService) { this.boardService = boardService; }

    @Operation(summary = "게시글 단건 조회", description = "게시글 ID로 삭제되지 않은 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getById(id));
    }

    @Operation(summary = "게시글 목록 조회", description = "게시글을 페이지 단위로 조회하며 제목 검색을 지원합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<Page<BoardResponse>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(boardService.getPaged(page, keyword));
    }

    @Operation(summary = "게시글 작성", description = "인증된 회원이 게시글을 작성합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "인증 필요",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BoardCreateResponse> create(
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody BoardCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.create(loginMember.memberId(), request));
    }

    @Operation(summary = "게시글 수정", description = "게시글 작성자가 제목 또는 내용을 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "인증 또는 수정 권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody BoardUpdateRequest request) {
        return ResponseEntity.ok(boardService.update(id, loginMember.memberId(), request));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 작성자가 게시글을 소프트 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "인증 또는 삭제 권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        boardService.delete(id, loginMember.memberId());
        return ResponseEntity.noContent().build();
    }
}
