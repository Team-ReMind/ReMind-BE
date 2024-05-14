package com.remind.api.mood.dto.response;

import com.remind.core.domain.mood.enums.FeelingType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기분 별 활동 차트 속 기분 비율 응답 model")
public record MoodPercentResponseDto(
        @Schema(description = "기분 종류")
        FeelingType feelingType,
        @Schema(description = "해당 기분이 차지하는 퍼센트")
        Double percent
) {
}
