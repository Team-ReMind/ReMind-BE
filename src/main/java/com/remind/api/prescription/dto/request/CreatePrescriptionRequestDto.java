package com.remind.api.prescription.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "약 복용 정보 업데이트 요청 객체")

public record CreatePrescriptionRequestDto(
        @Schema(description = "처방 정보를 등록할 환자의 memberId")
        Long memberId,

        @Schema(description = "약 처방 기간")
        int period,

        @Schema(description = "약 처방 메모")
        String memo,

        @Schema(description = "아침 약 중요도(0~3)")
        int breakfastImportance,

        @Schema(description = "점심 약 중요도(0~3)")
        int lunchImportance,

        @Schema(description = "저녁 약 중요도(0~3)")
        int dinnerImportance


) {
}
