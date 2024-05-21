package com.remind.api.mood.controller;

import com.remind.api.mood.dto.response.ActivityPercentResponseDto;
import com.remind.api.mood.dto.response.MoodChartPagingResponseDto;
import com.remind.api.mood.dto.response.MoodPercentResponseDto;
import com.remind.api.mood.service.MoodChartService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.domain.mood.enums.FeelingType;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mood/chart")
@Tag(name = "무드 차트 API", description = "무드 차트, 기분 별 활동 차트 API입니다.")
public class MoodChartController {

    private final MoodChartService moodChartService;

    @Operation(
            summary = "무드 차트 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "무드 차트 조회 성공 응답입니다.", useReturnTypeSchema = true
    )
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<MoodChartPagingResponseDto>> getMoodChart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "년도") @RequestParam("year") Integer year,
            @Parameter(description = "월") @RequestParam("month") Integer month,
            @Parameter(description = "마지막으로 조회한 일") @RequestParam("day") Integer day,
            @Parameter(description = "한 페이지 속 데이터 갯수") @RequestParam("size") Integer size) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(moodChartService.getMoodChart(userDetails, year, month, day, size)));
    }

    @Operation(
            summary = "기분 별 활동 차트 안의 기분 percent 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "기분 별 활동 차트 안의 기분 percent 조회 성공 응답입니다.", useReturnTypeSchema = true
    )
    @GetMapping("/percents")
    public ResponseEntity<ApiSuccessResponse<List<MoodPercentResponseDto>>> getMoodChartPercents(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(moodChartService.getActivityChart(userDetails)));
    }

    @Operation(
            summary = "특정 기분에 대한 활동 퍼센트 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "특정 기분에 대한 활동 퍼센트 조회 성공 응답입니다.", useReturnTypeSchema = true
    )
    @GetMapping("/percent/activity")
    public ResponseEntity<ApiSuccessResponse<List<ActivityPercentResponseDto>>> getActivityPercentChart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "감정") @RequestParam("feelingType") FeelingType feelingType) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(moodChartService.getActivityPercentChart(userDetails, feelingType)));
    }


    @Operation(
            summary = "무드 연속 기록 조회 API"
    )
    @ApiResponse(
            responseCode = "200", description = "무드 연속 기록 조회 성공 응답입니다.",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = "{\"code\":200, \"message:\": \"정상 처리되었습니다.\", \"data\": {\"currentSeriesDays\": 10}}")
                    }
            )
    )
    @GetMapping("/series")
    public ResponseEntity<ApiSuccessResponse<?>> getMaxSeries(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(Map.of("currentSeriesDays", moodChartService.getCurrentSeries(userDetails))));
    }
}
