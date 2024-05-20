package com.remind.api.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PatientDto(
        @Schema(description = "멤버 아이디")
        Long memberId,

        @Schema(description = "멤버 이름")
        String name,

        @Schema(description = "멤버 성별")
        String gender,

        @Schema(description = "멤버 나이")
        int age

        //추가 예정
) {
}
