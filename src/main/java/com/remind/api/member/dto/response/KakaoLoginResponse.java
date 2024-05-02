package com.remind.api.member.dto.response;

import lombok.Builder;

@Builder
public record KakaoLoginResponse(Long authId, String redirectUrl, String refreshToken, String accessToken, String name) {
}




