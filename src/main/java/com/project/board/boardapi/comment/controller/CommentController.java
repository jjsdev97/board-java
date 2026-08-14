package com.project.board.boardapi.comment.controller;

import com.project.board.boardapi.auth.security.LoginMember;
import com.project.board.boardapi.comment.dto.CommentResponse;
import com.project.board.boardapi.comment.dto.CommentUpdateRequest;
import com.project.board.boardapi.comment.service.CommentService;
import com.project.board.boardapi.comment.dto.CommentCreateRequest;
import com.project.board.boardapi.comment.dto.CommentCreateResponse;
import com.project.board.boardapi.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
@Tag(name = "댓글", description = "게시글 댓글 생성, 조회, 수정 및 삭제 API")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 작성", description = "인증된 회원이 게시글에 댓글을 작성합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패 또는 대댓글 깊이 초과",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "인증 필요",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글, 회원 또는 부모 댓글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CommentCreateResponse> create(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.create(loginMember.memberId(), boardId, request));
    }

    @Operation(summary = "댓글 단건 조회", description = "게시글에 속한 댓글을 댓글 ID로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable Long boardId,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(boardId, id));
    }

    @Operation(summary = "게시글 댓글 목록 조회", description = "게시글에 속한 삭제되지 않은 댓글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getByBoardId(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getByBoardId(boardId));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 작성자가 댓글을 소프트 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "인증 또는 삭제 권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long boardId,
                                       @PathVariable Long id,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        commentService.delete(boardId, id, loginMember.memberId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 수정", description = "댓글 작성자가 댓글 내용을 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "인증 또는 수정 권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
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
