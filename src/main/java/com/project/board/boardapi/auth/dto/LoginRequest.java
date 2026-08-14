package com.project.board.boardapi.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "회원 이메일", example = "user@example.com")
        @NotBlank(message = "이메일 필수 입력") @Email(message = "이메일 형식 불일치") String email,
        @Schema(description = "회원 비밀번호", example = "password1234")
        @NotBlank(message = "비밀번호 필수 입력") String password
) {}
