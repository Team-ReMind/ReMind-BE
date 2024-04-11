package com.remind.api.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
        @NotBlank(message = "refresh token 값은 필수입니다.")
        String refreshToken
) {
}
