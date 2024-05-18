package com.remind.api.connection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "환자 -> 의사/센터 관계 요청하는 응답 객체")
public record RequestConnectionResponseDto(
        @Schema(description = "요청을 보내며 생긴 요청 row의 아이디")
        Long ConnectionId) {
}
