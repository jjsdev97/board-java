package com.project.board.boardapi.member.controller;

import com.project.board.boardapi.member.dto.MemberCreateRequest;
import com.project.board.boardapi.member.dto.MemberCreateResponse;
import com.project.board.boardapi.member.dto.MemberResponse;
import com.project.board.boardapi.member.service.MemberService;
import com.project.board.boardapi.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원", description = "회원 가입 및 조회 API")
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) { this.memberService = memberService; }

    @Operation(summary = "회원 단건 조회", description = "회원 ID로 삭제되지 않은 회원을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "인증 필요",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    @Operation(summary = "회원 가입", description = "새로운 회원을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<MemberCreateResponse> create(@Valid @RequestBody MemberCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
    }
}
