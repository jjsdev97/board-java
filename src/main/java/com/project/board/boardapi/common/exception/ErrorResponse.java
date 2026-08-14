package com.project.board.boardapi.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 오류 응답")
public record ErrorResponse(
        @Schema(description = "오류 코드", example = "COMMON_400") String code,
        @Schema(description = "오류 메시지", example = "잘못된 요청입니다.") String message
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}
