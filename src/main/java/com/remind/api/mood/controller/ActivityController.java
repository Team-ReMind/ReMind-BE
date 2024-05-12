package com.remind.api.mood.controller;

import com.remind.api.mood.dto.request.ActivitySaveRequestDto;
import com.remind.api.mood.dto.response.ActivityListResponseDto;
import com.remind.api.mood.service.ActivityService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
@Tag(name = "활동 API", description = "활동 관련 API입니다.")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(
            summary = "활동 추가 API"
    )
    @ApiResponse(
            responseCode = "201", description = "활동 추가 성공 응답입니다.",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = "{\"code\":201, \"message:\": \"정상 처리되었습니다.\", \"data\": {\"activityId\": 1}}")
                    }
            )
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<?>> saveActivity(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ActivitySaveRequestDto dto) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(Map.of("activityIdd", activityService.save(userDetails, dto))));
    }

    @Operation(
            summary = "추가한 활동 리스트 조회 API"
    )
    @ApiResponse(
            responseCode = "200", description = "추가한 활동 리스트 조회 성공 응답입니다.", useReturnTypeSchema = true
    )
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<ActivityListResponseDto>> getActivityList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(activityService.getActivityList(userDetails)));
    }

}
