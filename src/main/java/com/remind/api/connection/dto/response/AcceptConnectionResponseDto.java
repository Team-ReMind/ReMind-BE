package com.remind.api.connection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "의사/센터가 환자의 관계 요청 응답을 수락하는 응답 객체")
public record AcceptConnectionResponseDto(
        @Schema(description = "요청을 수락하며 생긴 커넥션의 아이디")
        Long connectionId) {
}
