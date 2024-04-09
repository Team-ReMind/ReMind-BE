package com.remind.core.security.dto;

public record TokenResponseDto(
    String accessToken,
    String refreshToken
) {

}
