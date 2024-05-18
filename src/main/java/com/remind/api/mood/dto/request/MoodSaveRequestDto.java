package com.remind.api.mood.dto.request;

import com.remind.core.domain.mood.enums.FeelingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "오늘의 기분 생성 요청 model")
public record MoodSaveRequestDto(
        @Schema(description = "생성 날짜")
        @NotNull(message = "생성 날짜는 필수 값입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate localDate,
        @Schema(description = "오늘의 기분")
        FeelingType feelingType,
        @Schema(description = "오늘의 하루 활동들")
        @Nullable
        List<MoodActivityRequestDto> moodActivities,
        @Schema(description = "오늘 하루 감사한 점 3가지")
        String detail
) {
}