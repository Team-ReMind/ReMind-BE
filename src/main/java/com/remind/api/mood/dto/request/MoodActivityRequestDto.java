package com.remind.api.mood.dto.request;

import com.remind.core.domain.mood.enums.FeelingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "오늘의 기분 속 활동 정보")
public record MoodActivityRequestDto(
        @Schema(description = "활동 ID")
        @NotNull(message = "활동 ID는 필수 값입니다.")
        Long activityId,
        @Schema(description = "활동을 하며 느낀 기분 VERY_GOOD(매우 좋음), GOOD(좋음), NORMAL(보통), BAD(나쁨),TERRIBLE(끔찍함)")
        @NotBlank(message = "느낀 기분은 필수 입니다.")
        FeelingType feelingType,
        @Schema(description = "활동을 하며 느낀 것 메모")
        String detail
) {
}
