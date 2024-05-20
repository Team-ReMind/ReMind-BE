package com.remind.api.takingMedicine.controller;

import com.remind.api.takingMedicine.dto.request.CreateTakingMedicineRequest;
import com.remind.api.takingMedicine.dto.response.CreateTakingMedicineResponse;
import com.remind.api.takingMedicine.dto.response.DailyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.MonthlyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.TakingMedicineRateResponse;
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
            summary = "memberId(), 날짜로 해당 날짜의 약 복용 정보를 조회하는 api\n",
            description = "나의 정보를 조회하는 경우에는 memberId = 0 을 넣어주세요.\n" +
                    "date는 string으로, YYYY-MM-DD 형식으로 주세요."
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
            summary = "memberId, year, month로 월 단위 약 복용 정보를 조회하는 api <br> g",
            description = "해당 월 1일부터 말일까지의 데이터가 모두 들어있습니다. <br> g" +
                    " needMedicine : false인 경우, 약을 복용하지 않아도 되는 날 <br> g" +
                    " 나의 정보를 조회하는 경우에는 memberId = 0 을 넣어주세요 <br> g" +
                    "taking level : 약 복용 정도(0 : 미복용, 1 : 부분 복용, 2 : 모두 복용 완료 <br> g" +
                    "taking count :  그 날 약 복용 횟수, 단순 참고용"
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
            description = "medicineType : BREAKFAST, LUNCH, DINNER, ETC <br> " +
                    "약을 복용하지 않은 경우 notTakingReason을 비워두거나 빈 문자열"
    )
    @PostMapping("")
    public ResponseEntity<ApiSuccessResponse<CreateTakingMedicineResponse>> createTakingMedicine(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateTakingMedicineRequest req
    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(takingMedicineService.createTakingMedicine(userDetails, req)));
    }


    @Operation(
            summary = "특정 멤버의 약 복용률을 반환하는 api",
            description = "특정 날짜의 약 복용률 조회 api <br> 나의 정보를 조회하는 경우에는 memberId = 0 을 넣어주세요\""
    )
    @GetMapping("/rate")
    public ResponseEntity<ApiSuccessResponse<TakingMedicineRateResponse>> getTakingMedicineRate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long memberId
    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(takingMedicineService.getTakingMedicineRate(userDetails, memberId)));
    }
}
