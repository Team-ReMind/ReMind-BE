package com.remind.api.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "토큰 응답 model")
public record TokenResponseDto(
        @Schema(description = "access token 유효 기간: 30분")
        String accessToken,
        @Schema(description = "refresh token 유효 기간: 60분")
        String refreshToken
) {

}
