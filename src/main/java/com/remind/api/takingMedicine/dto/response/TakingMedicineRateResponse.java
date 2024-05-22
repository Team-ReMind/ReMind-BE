package com.remind.api.takingMedicine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "특정 멤버의 약 복용률 반환")
public record TakingMedicineRateResponse(
        @Schema(description = "아침 복용률")
        Double breakfastRate,

        @Schema(description = "점심 복용률")
        Double lunchRate,
        @Schema(description = "저녁 복용률")
        Double dinnerRate,

        @Schema(description = "전체 복용률")
        Double totalRate
        ) {
}
