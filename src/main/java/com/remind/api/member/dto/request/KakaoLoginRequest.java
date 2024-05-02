package com.remind.api.member.dto.request;

public record KakaoLoginRequest(String kakaoAccessToken, String redirectUrl) {
}
