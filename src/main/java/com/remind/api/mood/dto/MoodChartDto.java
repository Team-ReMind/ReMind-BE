package com.remind.api.mood.dto;

import com.remind.core.domain.mood.enums.FeelingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoodChartDto {

    @Schema(description = "날짜")
    private LocalDate localDate;
    @Schema(description = "해당 날짜의 기분")
    private FeelingType feeling;

    @Getter
    @Schema(description = "무드 차트 데이터")
    public static class MoodChartResponseDto {

        @Schema(description = "날짜")
        private final LocalDate localDate;
        @Schema(description = "해당 날짜의 기분")
        private final String feeling;
        @Schema(description = "기분에 대한 점수")
        private final Integer score;

        private MoodChartResponseDto(FeelingType feelingType, LocalDate localDate) {
            this.localDate = localDate;
            this.feeling = feelingType.getFeeling();
            this.score = feelingType.getScore();
        }
    }

    public MoodChartResponseDto toResponseDto(FeelingType feelingType, LocalDate localDate) {
        return new MoodChartResponseDto(feelingType, localDate);
    }
}
