package com.remind.api.takingMedicine.dto.response;

import com.remind.api.takingMedicine.dto.DailyTakingMedicineDto;
import com.remind.api.takingMedicine.dto.MonthlyTakingMedicineDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "특정 월 약 복용 정보를 반환할 dto list")
public record MonthlyTakingMedicineInfoResponse(
        @Schema(description = "특정 월에서 특정 날짜의 약 복용 정보를 반환할 리스트")
        List<MonthlyTakingMedicineDto> monthlyTakingMedicineDtos,

        @Schema(description = "아침 복용률")
        Double breakfastRatio,

        @Schema(description = "점심 복용률")
        Double lunchRatio,
        @Schema(description = "저녁 복용률")
        Double dinnerRatio,

        @Schema(description = "전체 복용률")
        Double totalRatio

        ) {
}
