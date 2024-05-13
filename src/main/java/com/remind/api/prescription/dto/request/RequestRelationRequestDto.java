package com.remind.api.prescription.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "환자 -> 의사 관계 요청하는 요청 객체")
public record RequestRelationRequestDto(
        @Schema(description = "요청하려는 의사의 멤버코드")
        String doctorMemberCode) {
}
