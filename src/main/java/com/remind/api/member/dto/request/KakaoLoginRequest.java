package com.remind.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record KakaoLoginRequest(
        @Schema(description = "카카오에서 발급받은 액세스 토큰", required = true)
        String kakaoAccessToken) {
}
