package com.remind.api.prescription.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "환자 관리 페이지에서 약 처방 정보")
public record PrescriptionInfoResponseDto(

        @Schema(description = "값 존재 여부")
        Boolean isExist,

        @Schema(description = "처방 정보 식별Id")
        Long prescriptionId,
        @Schema(description = "환자의 이름")
        String name,
        @Schema(description = "처방 날짜")
        LocalDate prescriptionDate,
        @Schema(description = "처방 기간")
        int period,
        @Schema(description = "아침 약 중요도")
        int breakfastImportance,
        @Schema(description = "점심 약 중요도")
        int lunchImportance,
        @Schema(description = "저녁 약 중요도")
        int dinnerImportance,

        @Schema(description = "기타 약 중요도")
        int etcImportance,

        @Schema(description = "처방 메모")
        String memo


) {
}
