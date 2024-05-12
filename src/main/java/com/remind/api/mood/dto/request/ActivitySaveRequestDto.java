package com.remind.api.mood.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "활동 추가 요청 model")
public record ActivitySaveRequestDto(
        @Schema(description = "활동명")
        String name,
        @Schema(description = "아이콘 이미지")
        String iconImage
) {
}
