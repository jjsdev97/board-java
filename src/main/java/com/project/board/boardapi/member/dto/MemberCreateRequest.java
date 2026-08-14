package com.project.board.boardapi.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberCreateRequest(
        @Schema(description = "회원 이메일", example = "user@example.com")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.") String email,
        @Schema(description = "회원 이름", example = "홍길동")
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 30, message = "이름은 30자 이하여야합니다.") String name,
        @Schema(description = "회원 비밀번호", example = "password1234")
        @NotBlank(message = "비밀번호을 입력해주세요.")
        @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다.") String password,
        @Schema(description = "회원 나이", example = "20", minimum = "1", maximum = "150")
        @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
        @Max(value = 150, message = "나이는 150 이하여야 합니다.") int age
) {}
