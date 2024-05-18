package com.remind.api.connection.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "환자 -> 의사/센터 관계 요청하는 요청 객체")
public record RequestConnectionRequestDto(
        @Schema(description = "요청하려는 의사/센터의 멤버코드")
        String targetMemberCode) {
}
