package com.remind.api.takingMedicine.controller;

import com.remind.api.takingMedicine.dto.request.CreateTakingMedicineRequest;
import com.remind.api.takingMedicine.dto.response.CreateTakingMedicineResponse;
import com.remind.api.takingMedicine.dto.response.DailyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.MonthlyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.service.TakingMedicineService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/taking-medicine")
@Slf4j
@Tag(name = "TakingMedicine(약 복용 정보) API", description = "TakingMedicine(약 복용 정보) API 문서")

public class TakingMedicineController {
    private final TakingMedicineService takingMedicineService;
    @Operation(
            summary = "memberId(), 날짜로 해당 날짜의 약 복용 정보를 조회하는 api",
            description = "memberId(), 날짜로 해당 날짜의 약 복용 정보를 조회하는 api\n 나의 정보를 조회하는 경우에는 memberId = 0 을 넣어주세요"
    )
    @GetMapping("/daily")
    public ResponseEntity<ApiSuccessResponse<DailyTakingMedicineInfoResponse>> getDailyTakingMedicineInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long memberId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(takingMedicineService.getDailyTakingMedicineInfo(userDetails, memberId, date)));
    }

    @Operation(
            summary = "memberId, year, month로 월 단위 약 복용 정보를 조회하는 api",
            description = "memberId, year, month로 월 단위 약 복용 정보를 조회하는 api\n 나의 정보를 조회하는 경우에는 memberId = 0 을 넣어주세요\""
    )
    @GetMapping("/monthly")
    public ResponseEntity<ApiSuccessResponse<MonthlyTakingMedicineInfoResponse>> getMonthlyTakingMedicineInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long memberId,
            @RequestParam int year,
            @RequestParam int month

    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(takingMedicineService.getMonthlyTakingMedicineInfo(userDetails, memberId, year,month)));
    }

    @Operation(
            summary = "특정 날짜의 약 복용 정보를 등록하는 api",
            description = "특정 날짜의 약 복용 정보를 등록하는 api"
    )
    @PostMapping("")
    public ResponseEntity<ApiSuccessResponse<CreateTakingMedicineResponse>> createTakingMedicine(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateTakingMedicineRequest req
    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(takingMedicineService.createTakingMedicine(userDetails, req)));
    }

}
