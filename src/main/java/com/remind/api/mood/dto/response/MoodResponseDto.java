package com.remind.api.mood.dto.response;

import com.remind.core.domain.mood.enums.FeelingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "특정 날짜 기분 조회 응답 model")
public record MoodResponseDto(
        @Schema(description = "오늘의 기분 종류 VERY_GOOD(매우 좋음), GOOD(좋음), NORMAL(보통), BAD(나쁨),TERRIBLE(끔찍함)")
        FeelingType feelingType,
        @Schema(description = "오늘의 하루 감사한 점")
        String moodDetail,
        @Schema(description = "오늘의 하루 활동들")
        List<ModelActivityResponseDto> activities

) {
}
