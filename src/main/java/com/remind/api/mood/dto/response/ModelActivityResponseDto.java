package com.remind.api.mood.dto.response;

import com.remind.core.domain.mood.enums.FeelingType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오늘의 활동 model")
public record ModelActivityResponseDto(
        @Schema(description = "활동 이름")
        String name,
        @Schema(description = "활동 아이콘 이미지")
        String iconImg,
        @Schema(description = "활동에서 느낀 기분")
        FeelingType feelingType,
        @Schema(description = "활동 느낀 점")
        String detail

) {
}
