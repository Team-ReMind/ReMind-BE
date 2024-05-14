package com.remind.api.mood.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기분 별 활동 응답 model")
public record ActivityPercentResponseDto(
        @Schema(description = "활동 이름")
        String name,
        @Schema(description = "활동 아이콘 이미지")
        String iconImage,
        @Schema(description = "활동 퍼센트")
        Double percent
) {
}
