package com.remind.api.prescription.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Schema(description = "환자 -> 의사 관계 요청하는 응답 객체")
public record RequestRelationResponseDto(
        @Schema(description = "요청을 보내며 생긴 관계(처방)의 아이디")
        Long PrescriptionId) {
}
