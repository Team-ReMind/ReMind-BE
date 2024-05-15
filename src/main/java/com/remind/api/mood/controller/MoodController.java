package com.remind.api.mood.controller;

import static com.remind.core.domain.common.enums.GlobalSuccessCode.*;

import com.remind.api.mood.dto.request.MoodSaveRequestDto;
import com.remind.api.mood.dto.response.MoodResponseDto;
import com.remind.api.mood.service.MoodService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
@RequestMapping("/mood")
@Tag(name = "오늘의 기분 API", description = "오늘의 기분 관련 API입니다.")
public class MoodController {

    private final MoodService moodService;

    @Operation(
            summary = "오늘의 기분 추가 API"
    )
    @ApiResponse(
            responseCode = "201", description = "오늘의 기분 추가 성공 응답입니다.",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = "{\"code\":201, \"message:\": \"정상 처리되었습니다.\", \"data\": {\"moodId\": 1}}")
                    }
            )
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<?>> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody MoodSaveRequestDto moodSaveRequestDto) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(Map.of("moodId", moodService.create(userDetails, moodSaveRequestDto))));
    }

    @Operation(
            summary = "특정 날짜의 오늘의 기분 정보 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "오늘의 기분 조회 성공 응답입니다.", useReturnTypeSchema = true
    )
    @GetMapping("/{moodDate}")
    public ResponseEntity<ApiSuccessResponse<MoodResponseDto>> get(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("moodDate") LocalDate localDate) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(SUCCESS, moodService.get(userDetails, localDate)));
    }
}
