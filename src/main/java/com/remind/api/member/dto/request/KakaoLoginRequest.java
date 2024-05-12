package com.remind.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record KakaoLoginRequest(
        @Schema(description = "카카오에서 발급받은 액세스 토큰", required = true)
        String kakaoAccessToken,

        @Schema(description = "FCM 서버에서 발급 받은 fcm", required = true)
        String fcmToken) {

}
