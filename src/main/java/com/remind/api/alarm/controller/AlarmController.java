package com.remind.api.alarm.controller;

import static org.springframework.http.HttpStatus.*;

import com.remind.api.alarm.dto.request.AlarmCreateRequestDto;
import com.remind.api.alarm.dto.response.AlarmListResponseDto;
import com.remind.api.alarm.service.AlarmService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
@Tag(name = "알림 API")
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(
            summary = "알림 추가 API"
    )
    @ApiResponse(
            responseCode = "201", description = "알림 추가 성공 응답입니다.",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = "{\"code\":201, \"message:\": \"정상 처리되었습니다.\", \"data\": {\"alarmId\": 1}}")
                    }
            )
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<?>> createAlarm(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AlarmCreateRequestDto dto) {
        return ResponseEntity.status(CREATED)
                .body(new ApiSuccessResponse<>(Map.of("alarmId", alarmService.createAlarm(userDetails, dto))));
    }


    @Operation(
            summary = "알림 목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "알림 목록 조회 성공 응답입니다.", useReturnTypeSchema = true
    )
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<ApiSuccessResponse<List<AlarmListResponseDto>>> getAlarms(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "처방전 ID") @PathVariable("prescriptionId") Long prescriptionId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(alarmService.getAlarms(userDetails, prescriptionId)));
    }
}
