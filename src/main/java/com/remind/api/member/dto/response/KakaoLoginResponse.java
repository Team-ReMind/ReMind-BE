package com.remind.api.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record KakaoLoginResponse(@Schema(description = "Remind의 리프레시 토큰")
                                 String refreshToken,

                                 @Schema(description = "Remind의 액세스 토큰")
                                 String accessToken,

                                 @Schema(description = "사용자가 온보딩 과정을 완료했는지 여부")
                                 Boolean isMemberFinishedOnboarding,
                                 @Schema(description = "카카오 사용자의 본명")
                                 String name) {
}




