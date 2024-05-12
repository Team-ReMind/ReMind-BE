package com.remind.api.mood.dto.response;

import com.remind.api.mood.dto.MoodChartDto.MoodChartResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "무드 차트 한 달 응답 데이터 model")
public record MoodChartPagingResponseDto(
        @Schema(description = "한 달 기분 정보 리스트")
        List<MoodChartResponseDto> moodChartDtos,
        @Schema(description = "다음 페이지 존재 여부")
        Boolean hasNext
) {
}
