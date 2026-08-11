package com.project.board.boardapi.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "이메일 필수 입력") @Email(message = "이메일 형식 불일치") String email,
        @NotBlank(message = "비밀번호 필수 입력") String password
) {}
