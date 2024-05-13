package com.remind.api.prescription.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "의사가 환자의 관계 요청을 수락하는 Request 객체")
public record AcceptRelationRequestDto(
        @Schema(description = "요청을 받아들일 환자의 memberId")
        Long memberId) {
}
