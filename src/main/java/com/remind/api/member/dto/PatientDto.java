package com.remind.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PatientDto(
        @Schema(description = "멤버 아이디")
        Long memberId,

        @Schema(description = "멤버 이름")
        String name

        //추가 예정
) {
}
